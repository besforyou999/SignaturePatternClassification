
import base64
from collections import OrderedDict
import json
import math
import mysql.connector
import numpy as np
import socket, threading;
#from PIL import Image
#import io
#import glob

#################################################
# Input: json
# Ouput: lists
def read_json(json):

    X = json['xPos']
    Y = json['yPos']
    T = json['time']
    P = json['isFirst']

    StartPoint = []
    for idx in range(len(P)):
        if P[idx] == 0 : StartPoint.append(idx)

    return X, Y, T, StartPoint

#################################################
# Input: lists about signature
# Ouput: lists normalize by regular time interval
def time_normalization(StartPoint, X, Y, T):

    interval = 5 # ms 

    XT = []
    YT = []

  # Divide by stroke
    for i in range(len(StartPoint)):
        s = StartPoint[i]
        e = len(T) if i == (len(StartPoint) - 1) else StartPoint[i + 1]

        stroke_x = np.array(X[s:e])
        stroke_y = np.array(Y[s:e])
        stroke_t = np.array(T[s:e]) - T[s]

        # Stroke Concatenation
        if i != 0 :
            stroke_x = stroke_x - stroke_x[0] + XT[-1]
            stroke_y = stroke_y - stroke_y[0] + YT[-1]

        # Interpolate the stroke by time 
        t = 0

        for k in range(len(stroke_t) - 1):
            while(stroke_t[k] <= t and t < stroke_t[k+1]):
                x = stroke_x[k] + (stroke_x[k+1] - stroke_x[k])*(t - stroke_t[k])
                y = stroke_y[k] + (stroke_y[k+1] - stroke_y[k])*(t - stroke_t[k])
                XT.append(x)
                YT.append(y)
                t = t + interval

        if stroke_t[-1] == t:
            XT.append(stroke_x[-1])
            YT.append(stroke_y[-1])
        
    return XT, YT

#################################################
# Input: lists of X-axis and Y-axis
# Ouput: lists of derivative
def get_derivative(X, Y):
  
    X1 = []
    Y1 = []

    for i in range(len(X) - 1) :
        X1.append(X[i+1] - X[i])
        Y1.append(Y[i+1] - Y[i])

    return X1, Y1

#################################################
# Input: (list)1st derivative of X-axis and Y-axis, 2nd derivative of X-axis and Y-axis
# Ouput: (np.array)histogram vector about signature feature
def make_histogram(X1, Y1, X2, Y2):

    theta1 = []
    theta2 = []
    R1 = []
    R2 = []
    F = ()

    X3, Y3 = get_derivative(X2, Y2)
    theta3 = []
    R3 = []

    ep = 0.000001

    for i in range(len(X1)):
        if X1[i] == 0 :
            theta1.append(np.arctan(Y1[i]/(X1[i]+ep)))
        else :
            theta1.append(np.arctan(Y1[i]/X1[i]))
        R1.append(math.sqrt(math.pow(X1[i],2)+math.pow(Y1[i],2)))

    for i in range(len(X2)):
        if X2[i] == 0 :
            theta2.append(np.arctan(Y2[i]/(X2[i]+ep)))
        else :
            theta2.append(np.arctan(Y2[i]/X2[i]))
        R2.append(math.sqrt(math.pow(X2[i],2)+math.pow(Y2[i],2)))

    for i in range(len(X3)):
        if X3[i] == 0 :
            theta3.append(np.arctan(Y3[i]/(X3[i]+ep)))
        else :
            theta3.append(np.arctan(Y3[i]/X3[i]))
        R3.append(math.sqrt(math.pow(X3[i],2)+math.pow(Y3[i],2)))

    # Relative
    hist, bin_edges = np.histogram(theta1, 8, range=(-math.pi/2, math.pi/2), density=True)  # theta1
    F = np.append(F, hist, axis=0)
    hist, bin_edges = np.histogram(theta2, 12, range=(-math.pi/2, math.pi/2), density=True) # theta2
    F = np.append(F, hist, axis=0)
    hist, bin_edges = np.histogram(X1, 8, range=(np.mean(X1) - 3*np.std(X1), np.mean(X1) + 3*np.std(X1)), density=True) # X1
    F = np.append(F, hist, axis=0)
    hist, bin_edges = np.histogram(Y1, 8, range=(np.mean(Y1) - 3*np.std(Y1), np.mean(Y1) + 3*np.std(Y1)), density=True) # Y1
    F = np.append(F, hist, axis=0)
    hist, bin_edges = np.histogram(X2, 8, range=(np.mean(X2) - 3*np.std(X2), np.mean(X2) + 3*np.std(X2)), density=True) # X2
    F = np.append(F, hist, axis=0)
    hist, bin_edges = np.histogram(Y2, 8, range=(np.mean(Y2) - 3*np.std(Y2), np.mean(Y2) + 3*np.std(Y2)), density=True) # Y2
    F = np.append(F, hist, axis=0)
    hist, xedges, yedges = np.histogram2d(X1[:-1], X2, bins=([6, 4]) 
        ,range=([[np.mean(X1[:-1]) - 3*np.std(X1[:-1]), np.mean(X1[:-1]) + 3*np.std(X1[:-1])], [np.mean(X2) - 3*np.std(X2), np.mean(X2) + 3*np.std(X2)]]), density=True)  # <X1, X2>
    hist, xedges, yedges = np.histogram2d(Y1[:-1], Y2, bins=([6, 4]) 
        ,range=([[np.mean(Y1[:-1]) - 3*np.std(Y1[:-1]), np.mean(Y1[:-1]) + 3*np.std(Y1[:-1])], [np.mean(Y2) - 3*np.std(Y2), np.mean(Y2) + 3*np.std(Y2)]]), density=True)  # <Y1, Y2>
    F = np.append(F, hist.flatten(), axis=0)

    h1 = math.floor(len(X1)/2)
    h2 = math.floor(len(X2)/2)
    h3 = math.floor(len(X3)/2)
    hist, xedges, yedges = np.histogram2d(theta1[:h1], R1[:h1], bins=([4, 4]), 
        range=([[-math.pi/2, math.pi/2], [0, np.mean(R1[:h1]) + 3*np.std(R1[:h1])]]), density=True)  # <theta1(1), R1(1)>
    F = np.append(F, hist.flatten(), axis=0)
    hist, xedges, yedges = np.histogram2d(theta1[h1:], R1[h1:], bins=([4, 4]),
        range=([[-math.pi/2, math.pi/2], [0, np.mean(R1[h1:]) + 3*np.std(R1[h1:])]]), density=True)  # <theta1(2), R1(2)>
    F = np.append(F, hist.flatten(), axis=0)
    hist, xedges, yedges = np.histogram2d(theta2[:h2], R2[:h2], bins=([4, 4]),
        range=([[-math.pi/2, math.pi/2], [0, np.mean(R2[:h2]) + 3*np.std(R2[:h2])]]), density=True)  # <theta2(1), R2(1)>
    F = np.append(F, hist.flatten(), axis=0)
    hist, xedges, yedges = np.histogram2d(theta2[h2:], R2[h2:], bins=([4, 4]),
        range=([[-math.pi/2, math.pi/2], [0, np.mean(R2[h2:]) + 3*np.std(R2[h2:])]]), density=True)  # <theta2(2), R2(2)>
    F = np.append(F, hist.flatten(), axis=0)
    hist, xedges, yedges = np.histogram2d((theta1[:-1])[:h2], R2[:h2], bins=([4, 4]),
        range=([[-math.pi/2, math.pi/2], [0, np.mean(R2[:h2]) + 3*np.std(R2[:h2])]]), density=True)  # <theta1(1), R2(1)>
    F = np.append(F, hist.flatten(), axis=0)
    hist, xedges, yedges = np.histogram2d((theta1[:-1])[h2:], R2[h2:], bins=([4, 4]),
        range=([[-math.pi/2, math.pi/2], [0, np.mean(R2[h2:]) + 3*np.std(R2[h2:])]]), density=True)  # <theta1(2), R2(2)>
    F = np.append(F, hist.flatten(), axis=0)

    # Relative add
    hist, xedges, yedges = np.histogram2d(X1[:-1], Y2, bins=([6, 4]),
        range=([[np.mean(X1[:-1]) - 3*np.std(X1[:-1]), np.mean(X1[:-1]) + 3*np.std(X1[:-1])], [np.mean(Y2) - 3*np.std(Y2), np.mean(Y2) + 3*np.std(Y2)]]), density=True)  # <X1, Y2>
    F = np.append(F, hist.flatten(), axis=0)
    hist, xedges, yedges = np.histogram2d(X2, Y1[:-1], bins=([6, 4]),
        range=([[np.mean(X2) - 3*np.std(X2), np.mean(X2) + 3*np.std(X2)], [np.mean(Y1[:-1]) - 3*np.std(Y1[:-1]), np.mean(Y1[:-1]) + 3*np.std(Y1[:-1])]]), density=True)  # <X2, Y1>
    F = np.append(F, hist.flatten(), axis=0)
    hist, bin_edges = np.histogram(theta3, 12, range=(-math.pi/2, math.pi/2), density=True)  # theta3
    F = np.append(F, hist, axis=0)
    
    # Absolute
    hist, xedges, yedges = np.histogram2d((theta1[:-1]+theta1[:-2]), (theta1[1:]+theta1[2:]), bins=([4, 4]), range=([[-math.pi/2, math.pi/2], [-math.pi/2, math.pi/2]]))  # <theta1, theta1 d(1,2)>
    F = np.append(F, hist.flatten(), axis=0)
    hist, bin_edges = np.histogram(R1, 16, range=(0, np.mean(R1) + 3*np.std(R1)))  # R1
    F = np.append(F, hist, axis=0)
    hist, bin_edges = np.histogram(R2, 16, range=(0, np.mean(R2) + 3*np.std(R2)))  # R2
    F = np.append(F, hist, axis=0)
    
    # Absolute add
    hist, bin_edges = np.histogram(theta1, 8, range=(-math.pi/2, math.pi/2))  # theta1
    F = np.append(F, hist, axis=0)
    hist, bin_edges = np.histogram(R3, 16, range=(0, np.mean(R3) + 3*np.std(R3)))  # R3
    F = np.append(F, hist, axis=0)

    hist, xedges, yedges = np.histogram2d(theta3[:h3], R3[:h3], bins=([4, 4]),
        range=([[-math.pi/2, math.pi/2], [0, np.mean(R3[:h3]) + 3*np.std(R3[:h3])]]), density=True)  # <theta3(1), R3(1)>
    F = np.append(F, hist.flatten(), axis=0)
    hist, xedges, yedges = np.histogram2d(theta3[h3:], R3[h3:], bins=([4, 4]),
        range=([[-math.pi/2, math.pi/2], [0, np.mean(R3[h3:]) + 3*np.std(R3[h3:])]]), density=True)  # <theta3(1), R3(1)>
    F = np.append(F, hist.flatten(), axis=0)

    return F

#################################################
# Input: json 
# Output: histogram vector about signature feature
def preprocess_json(json):

    X, Y, T, StartPoint = read_json(json)

    XT, YT = time_normalization(StartPoint, X, Y, T)
    X1, Y1 = get_derivative(XT, YT)  # time_normalization?????? stroke??? ??????????????? P ?????? X
    X2, Y2 = get_derivative(X1, Y1)

    F = make_histogram(X1, Y1, X2, Y2)

    return F 

#################################################
# Input: list of histogram vectors about signature features
# Output: quantization step vector (quantization step == standard deviation of histogram vectors)
def calc_user_Q(dataset):

    M = np.mean(dataset, axis=0)
    b = 1.5
    S = np.zeros(len(dataset[1]))

    for F in dataset:
        for i in range(len(F)):
            S[i] = S[i] + math.pow(F[i] - M[i], 2)

    Q = b * np.sqrt(S/len(dataset))
    
    return Q

#################################################
# Input: list of histogram vectors about signature features, quantization step vector
# Output: mean vector of quantized vectors
#         mean score of histogram vectors in dataset
#         standard deviation of scores
def calc_user_Fu(dataset, Q, len_abs):

    r_ep = 0.8
    a_ep = 0.002

    S = []
    temp = []

    for F in dataset:
        S.append((F / np.concatenate([Q[:-len_abs] + r_ep, Q[-len_abs:] + a_ep])).tolist())

    Fu = np.mean(S, axis=0)

    for F in dataset:
        Ft = F / np.concatenate([Q[:-len_abs] + r_ep, Q[-len_abs:] + a_ep])
        temp.append(np.sum(np.absolute(Ft - Fu)))
    
    m = np.mean(temp)
    d = np.std(temp)

    return Fu, m, d

#################################################
# Input: json about user signatures
# Output: success or failure of DB save
def save2db(jsonObj):

    name = jsonObj['name']
    mydb = mysql.connector.connect(
        #host="127.0.0.1",
        host="18.207.156.28",
        user="root",
        passwd="root",
        database="jhs"
    )
    cur = mydb.cursor()
    sql = (
        "SELECT * "
        "FROM user_information "
        "WHERE user_name = %s;"
    )
    data = (name,)
    cur.execute(sql, data)
    result = cur.fetchone()
    if cur.rowcount != 0:
        return -1

    dataset = []
    len_abs = 104
    
    for i in range(len(jsonObj)-1):
        F = preprocess_json(jsonObj[str(i)])
        dataset.append(F)

    dataset = np.array(dataset)
    Q = calc_user_Q(dataset)
    Fu, m, d = calc_user_Fu(dataset, Q, len_abs)

    data_json = OrderedDict()
    data_json["Q"] = Q.tolist()
    data_json["Fu"] = Fu.tolist()
    data_json["mean"] = m
    data_json["std"] = d
    sql = (
        "INSERT INTO user_information(user_name, data)"
        "VALUES (%s, %s);"
    )
    data = (name, json.dumps(data_json, ensure_ascii=False))
    cur.execute(sql, data)
    mydb.commit()
    cur.close()

    return 1


# binder????????? ???????????? accept??? ?????? ???????????? socket ??????????????? ?????? client??? ?????? ???????????? ????????? echo????????? ??????????????? ???????????????.
def binder(client_socket, addr):
    # ???????????? ?????? ?????? ????????? ?????????.
    print('Connected by', addr)
    try:
# ?????? ??????????????? ?????????????????? ?????? ?????? ???????????? ?????? ????????????.
# ?????? ????????? ????????? ????????? except??? ???????????? ????????? ????????? ??????.
        not_connected = True
        while not_connected:
            # socket??? recv????????? ????????? ?????????????????? ???????????? ?????? ???????????? ???????????????. ?????? 4???????????? ???????????????.
            data = client_socket.recv(4)
            if data is not None:
                not_connected = False
            # ?????? 4???????????? ????????? ???????????? ????????????. ??? ????????? little big ??????????????? byte?????? int???????????? ????????????.
            # C#??? BitConverter??? big??????????????? ????????????.
            length = int.from_bytes(data, "little")
            # ?????? ???????????? ????????????.
            data = client_socket.recv(length)
            # ????????? ???????????? str???????????? decode??????. (bytes to str)
            msg = data.decode()
            # base64 to bytes
            msg = base64.b64decode(msg)
            # bytes to string
            msg = str(msg)
            # Json in string??? ??????
            msg = msg[msg.find('{'):-1]
            # ????????? ???????????? ????????? ????????????.
            print('Received from', addr, msg)

            jsonObject = json.loads(msg)
            try:
                result = save2db(jsonObject)
                print("Success to save!")
            except:
                result = 0
                print("Fail to save...")

            # respond to str
            respond = str(result)
            # ????????????(byte)???????????? ????????????.
            respond_byte = respond.encode()
            # ??????????????? ????????? ???????????? ?????????.
            respond_byte_length = len(respond_byte)
            # ????????? ???????????? little ????????? ???????????? byte??? ????????? ?????? ????????????.
            client_socket.sendall(respond_byte_length.to_bytes(4, byteorder='little'))
            # ???????????? ?????????????????? ????????????.
            client_socket.sendall(respond_byte)


    except Exception as e:
    # ????????? ????????? except??? ????????????.
        print("except : " , addr)
        print(e)
    finally:
    # ????????? ????????? socket ???????????? ?????????.
        client_socket.close()

# Make socket and listen
print("= Set registration server socket")
# ????????? ?????????.
server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
# ?????? ????????? ????????? ????????? ????????????.
server_socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
# ????????? ?????? ip??? ???????????? pc??? ????????? ip??? ???????????? ????????? ????????? None??? ?????? ''??? ????????????.
# ????????? pc????????? ???????????? ????????? ????????????. cmd?????? netstat -an | find "LISTEN"?????? ????????? ??? ??????.
server_socket.bind(('', 9990))
# server ????????? ???????????? listen??? ????????????.
server_socket.listen()

# Start server thread
print("= Start thread")
try:
    # ????????? ?????? ?????????????????? ???????????? ????????? ?????? ????????? ????????????.
    while True:
        # client??? ????????? ???????????? accept??? ????????????.
        # ?????? client ????????? addr(??????)??? ????????? ?????????.
        client_socket, addr = server_socket.accept()
        th = threading.Thread(target=binder, args = (client_socket,addr))
        # ???????????? ???????????? client ?????? ????????? ????????? ?????? accept??? ???????????? ?????? client??? ????????????.
        th.start()
except:
    print("server")
finally:
    # ????????? ???????????? ?????? ????????? ?????????.
    server_socket.close()



