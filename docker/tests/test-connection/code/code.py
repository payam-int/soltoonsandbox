import socket, os

role = os.environ['ROLE']


def startServer():
    hosts = int(os.environ['HOSTS'])
    connected_hosts = 0
    port = int(os.environ['PORT'])
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
    s.bind(('0.0.0.0', port))
    s.listen(10)

    for i in range(0, hosts):
        c, addr = s.accept()
        r = c.recv(4096)

        if r == 'con':
            connected_hosts += 1
            c.sendall('ok')

    if connected_hosts == hosts:
        exit(0)
    else:
        exit(5)


def startClient():
    host = os.environ['HOST']
    port = int(os.environ['PORT'])
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s.connect((host, port))
    s.sendall('con')
    r = s.recv(2048)

    if r == 'ok':
        exit(0)
    else:
        exit(5)


if role == 'server':
    startServer()
else:
    startClient()
