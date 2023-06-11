import tool
import flask
import json

server = flask.Flask(__name__)
gt = tool.GraphTool()

@server.route('/Test', methods=['post', 'get'])
def test():
    s = flask.request.args.get('test')
    return s

# 127.0.0.1:8888/Word/hasWord?word={}
@server.route('/Word/hasWord', methods=['post', 'get'])
def hasWord():
    s = flask.request.args.get('word')
    res = {}
    try:
        res['InDataBase'] = gt.has_node(s, tool.Nodes.Word)
        res['res'] = True
    except:
        res['res'] = False
    return json.dumps(res, ensure_ascii=False)


# 127.0.0.1:8888/Word/GetInfo?word={}
@server.route('/Word/GetInfo', methods=['post', 'get'])
def getWordInfo():
    s = flask.request.args.get('word')
    res = {'name': s}

    try:
        temp = gt.get_node(s, tool.Nodes.Word)
        res['info'] = {}
        res['info']['attr_list'] = temp['attr_list']
        for i in temp['attr_list']:
            dict = {'value': temp[i],
                    'relev': gt.get_relationNode(s, tool.Nodes.Word, tool.Nodes.Word, i, tool.Rels.Relevancy)}
            res['info'][i] = dict
        res['res'] = True
    except:
        res['res'] = False

    return json.dumps(res, ensure_ascii=False)


# 127.0.0.1:8888/Word/Create?finish={1 or 0}&word={name}&attr={}&info={}
# finish为0表示还在输入各属性信息，为1表示当前提交信息为最后一个
# 属性唯一，终端判断
temp = {}
@server.route('/Word/Create', methods=['post', 'get'])
def createNewWord():
    global temp
    global rele
    flag = flask.request.args.get('finish')
    word = flask.request.args.get('word')
    attr = flask.request.args.get('attr')
    info = flask.request.args.get('info')
    res = {}

    if 'attr_list' not in temp:
        temp['attr_list'] = []
    temp['attr_list'].append(attr)
    temp[attr] = info

    if flag == "1":
        try:
            gt.create_node(tool.Nodes.Word, word, temp)
            res['res'] = True
        except:
            res['res'] = False

        res['done'] = True
        temp = {}
    else:
        res['res']=True
        res['done'] = False
    return json.dumps(res, ensure_ascii=False)


# 127.0.0.1:8888/Word/CreateRel?finish={1 or 0}&word={name}&attr={}&relev={"word1_word2"}
# 关联词条存在与否在终端判断，不存在的词条先跳转到新建词条页面，否则终端不通过新建申请
rele = {}
@server.route('/Word/CreateRel', methods=['post', 'get'])
def createNewRels():
    global rele
    flag = flask.request.args.get('finish')
    word = flask.request.args.get('word')
    attr = flask.request.args.get('attr')
    relev = flask.request.args.get('relev')
    res = {}

    if not (relev is None):
        rele[attr] = relev.split('_')
    else:
        rele[attr] = []

    if flag == "1":
        try:
            for i in rele:
                for j in rele[i]:
                    if j!= "":
                        gt.create_relationship(tool.Nodes.Word, tool.Nodes.Word, word, j, tool.Rels.Relevancy, i)
            res['res'] = True
        except:
            res['res'] = False

        res['done'] = True
        rele = {}
    else:
        res['res']=True
        res['done'] = False
    return json.dumps(res, ensure_ascii=False)


# 127.0.0.1:8888/Word/Del?word={}
@server.route('/Word/Del', methods=['post', 'get'])
def delWord():
    word = flask.request.args.get('word')
    res = {}
    try:
        gt.del_node(word, tool.Nodes.Word)
        res['name'] = word
        res['res'] = True
    except:
        res['res'] = False
    return json.dumps(res, ensure_ascii=False)


# 账号八位
# 127.0.0.1:8888/User/Login?account="account"&pw="password"
@server.route('/User/Login', methods=['post', 'get'])
def login():
    account = flask.request.args.get('account')
    pw = flask.request.args.get('pw')
    res = {}
    try:
        node = gt.get_node(account, tool.Nodes.User)
        password = node['password']
        res = {'account': node['name'], 'username': node['username'], 'isAdmin': node['isAdmin']}
        if pw == password:
            res['pass'] = True
        else:
            res['pass'] = False
        res['res'] = True
    except:
        res['pass']= False
        res['res'] = False
    return json.dumps(res, ensure_ascii=False)


# 127.0.0.1:8888/User/getInfo?account="account"
@server.route('/User/getInfo', methods=['post', 'get'])
def getInfo():
    res = {}
    account = flask.request.args.get('account')
    try:
        t = gt.get_node(account, tool.Nodes.User)
        res['account'] = t['account']
        res['username'] = t['username']
        res['isAdmin'] = t['isAdmin']
        res['res'] = True
    except:
        res['res'] = False
    return json.dumps(res, ensure_ascii=False)


# 127.0.0.1:8888/User/Del?account="account"
@server.route('/User/Del', methods=['post', 'get'])
def delUser():
    account = flask.request.args.get('account')
    res = {}
    try:
        temp = gt.get_relationNode(account, tool.Nodes.User, tool.Nodes.Favorite, account, tool.Rels.Have)
        for i in temp:
            gt.del_node(i, tool.Nodes.Favorite)
        gt.del_node(account, tool.Nodes.User)
        res['account'] = account
        res['res'] = True
    except:
        res['res'] = False
    return json.dumps(res, ensure_ascii=False)


# 127.0.0.1:8888/User/Registered?account="account"&pw="password"&username="username"&isAdmin={1 or 0}
@server.route('/User/Registered', methods=['post', 'get'])
def registered():
    res = {}
    account = flask.request.args.get('account')
    if gt.has_node(account, tool.Nodes.User):
        res['res'] = False
        res['info'] = '账号不唯一'
    else:
        pw = flask.request.args.get('pw')
        username = flask.request.args.get('username')
        admin = flask.request.args.get('isAdmin')
        info = {'password': pw, 'username': username}

        res['account'] = account
        res['username'] = username
        if admin == "1":
            info['isAdmin'] = True
            res['isAdmin'] = True
        else:
            info['isAdmin'] = False
            res['isAdmin'] = False
        try:
            gt.create_node(tool.Nodes.User, account, info)
            gt.create_node(tool.Nodes.Favorite, account+"默认收藏夹", {"label":"默认收藏夹"})
            gt.create_relationship(tool.Nodes.User, tool.Nodes.Favorite, account, account+"默认收藏夹", tool.Rels.Have, account)
            res['res'] = True
        except:
            res['res'] = False
            res['info'] = 'error'
    return json.dumps(res, ensure_ascii=False)


# 127.0.0.1:8888/User/setInfo?account={}&pw={}&username={}
@server.route('/User/setInfo', methods=['post', 'get'])
def setInfo():
    res = {}
    account = flask.request.args.get('account')
    pw = flask.request.args.get('pw')
    username = flask.request.args.get('username')
    info = {}
    res['username'] = username
    res['account'] = account
    if not (pw is None):
        info['password'] = pw
    if not (username is None):
        info['username'] = username
    try:
        gt.set_info(account, tool.Nodes.User, info)
        res['res'] = True
    except:
        res['res'] = False
    return json.dumps(res, ensure_ascii=False)


# 127.0.0.1:8888/Favorite/CreateFold?account="account"&foldName={}
@server.route('/Favorite/CreateFold', methods=['post', 'get'])
def CreateFold():
    account = flask.request.args.get('account')
    foldName = flask.request.args.get('foldName')
    name = account + foldName
    res = {'name': name}
    try:
        gt.create_node(tool.Nodes.Favorite, name)
        gt.create_relationship(tool.Nodes.User, tool.Nodes.Favorite, account, name, tool.Rels.Have, account)
        res['res'] = True
    except:
        res['res'] = False
    return json.dumps(res, ensure_ascii=False)


# 127.0.0.1:8888/Favorite/OperateWord?account="account"&foldName={}&do={add del}&word={}
@server.route('/Favorite/OperateWord', methods=['post', 'get'])
def OperateWord():
    account = flask.request.args.get('account')
    foldName = flask.request.args.get('foldName')
    do = flask.request.args.get('do')
    word = flask.request.args.get('word')
    name = account + foldName
    res = {}
    if do == 'add':
        res['op'] = 'add'
        try:
            gt.create_relationship(tool.Nodes.Favorite, tool.Nodes.Word, name, word, tool.Rels.Include, name)
            res['res'] = True
        except:
            res['res'] = False

        return json.dumps(res, ensure_ascii=False)
    elif do == 'del':
        res['op'] = 'del'
        try:
            gt.del_relationship(tool.Nodes.Favorite, tool.Rels.Include, tool.Nodes.Word, name, name, word)
            res['res'] = True
        except:
            res['res'] = False
        return json.dumps(res, ensure_ascii=False)


# 127.0.0.1:8888/Favorite/AllWord?account="account"&foldName={}
@server.route('/Favorite/AllWord', methods=['post', 'get'])
def getAllWord():
    res = {}
    account = flask.request.args.get('account')
    foldName = flask.request.args.get('foldName')
    name = account + foldName
    try:
        res['include'] = gt.get_relationNode(name, tool.Nodes.Favorite, tool.Nodes.Word, name, tool.Rels.Include)
        res['res'] = True
    except:
        res['res'] = False
    return json.dumps(res, ensure_ascii=False)


# 127.0.0.1:8888/Favorite/AllFold?account="account"
@server.route('/Favorite/AllFold', methods=['post', 'get'])
def getAllFold():
    res = {}
    account = flask.request.args.get('account')
    try:
        res['favorite'] = gt.get_relationNode(account, tool.Nodes.User, tool.Nodes.Favorite, account, tool.Rels.Have)
        res['res'] = True
    except:
        res['res'] = False

    return json.dumps(res, ensure_ascii=False)


# 127.0.0.1:8888/Favorite/Del?account="account"&foldName={}
@server.route('/Favorite/Del', methods=['post', 'get'])
def delFavorite():
    account = flask.request.args.get('account')
    foldName = flask.request.args.get('foldName')
    s = account + foldName
    res = {}
    try:
        gt.del_node(s, tool.Nodes.Favorite)
        res['foldName'] = s
        res['res'] = True
    except:
        res['res'] = False
    return json.dumps(res, ensure_ascii=False)


server.run(port=8888, debug=False, host='0.0.0.0')
