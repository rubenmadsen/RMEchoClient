import pickle


class RMEchoPacket:


    def __init__(self):
        self.flags = 0
        self.id = 0
        self.data_len = 0
        self.data = 0

    def makePacket(self,anObject):
        flagBytes = pickle.dumps(self.flags)
        idBytes = pickle.dumps(self.id)
        dataBytes = pickle.dumps(anObject)
        self.data_len = len(dataBytes)
        data_lenBytes = pickle.dumps(self.data_len)
        terminator = '\0'
        terminatorBytes = pickle.dumps(terminator)
        pbytes = [flagBytes + idBytes + data_lenBytes + dataBytes + terminatorBytes]

        return pbytes
