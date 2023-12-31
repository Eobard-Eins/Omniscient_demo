from py2neo import Graph, Node


class GraphTool:
    def __init__(self):
        self.g = Graph(
            host="127.0.0.1",  # neo4j 搭载服务器的ip地址，ifconfig可获取到
            # http_port=7474,  # neo4j 服务器监听的端口号
            user="neo4j",  # 数据库user name，如果没有更改过，应该是neo4j
            password="Server85426")

    def create_node(self, label, name, info={}):
        node = Node(label, name=name)
        for i in info:
            node[i] = info[i]
        self.g.create(node)

    '''
    start_node 起始节点名字
    end_node 终止节点名字
    start_label 起始节点label
    end_label  终止节点label
    rel_type 边label
    rel_name 边name
    '''

    def create_relationship(self, start_label, end_label, start_node, end_node, rel_type, rel_name):
        query = "match(p:%s),(q:%s) where p.name='%s'and q.name='%s' create (p)-[rel:%s{name:'%s'}]->(q)" % (
            start_label, end_label, start_node, end_node, rel_type, rel_name)
        self.g.run(query)

    '''
    获取节点信息
    '''

    def get_node(self, node, node_label):
        sql = "match (word:{0}) where word.name='{1}' return word".format(node_label, node)
        dic = self.g.run(sql).data()[0]['word']
        return dict(dic)

    def get_relationNode(self, node_start, node_label_start, node_label_end, rela, rela_label):
        sql = "match (word:{0})-[r:{1}]->(n:{2}) where word.name='{3}' and r.name='{4}' return n.name".format(
            node_label_start, rela_label, node_label_end, node_start, rela)
        dict = self.g.run(sql).data()
        res = []
        for data in dict:
            res.append(data['n.name'])
        return res

    def del_relationship(self, node_label_start, rela_label, node_label_end, node_start, rela, node_end):
        sql = "match (word:{0})-[r:{1}]->(n:{2}) where word.name='{3}' and r.name='{4}' and n.name='{5}' detach delete r".format(
            node_label_start, rela_label, node_label_end, node_start, rela, node_end)
        dict = self.g.run(sql)

    def set_info(self, node, node_label, info={}):
        for i in info:
            sql = 'match (p:{0}) where p.name="{1}" set p.{2}="{3}"'.format(node_label, node, i, info[i])
            self.g.run(sql)

    def del_node(self, node, node_label):
        sql = "match (p:{0}) where p.name='{1}' detach delete p".format(node_label, node)
        self.g.run(sql)

    def get_allWord(self):
        list = self.g.run("match (n:Word) return n.name").data()
        res = []
        for data in list:
            res.append(data['n.name'])
        return res

    def has_node(self, node, node_label):
        res = self.g.run("match (n:{0}) where n.name='{1}' return n.name".format(node_label, node)).data()
        if len(res) == 0:
            return False
        else:
            return True


class Nodes:
    Word = "Word"
    User = "User"
    Favorite = "Favorite"


class Rels:
    Relevancy = "Relevancy"  # 词条关联
    Have = "Have"  # user有收藏夹Favorite
    Include = "Include"  # 收藏夹Favorite包括词条Word


test = GraphTool()
# test.create_node(Nodes.Word,"T6",info)
# test.del_relationNode('10000001',Nodes.User,Nodes.Favorite,"10000001",Rels.Have)
# test.create_relationship(Nodes.Word,Nodes.Word,"T1","T2",Rels.Relevancy,"attr1")
# test.create_relationship(Nodes.Word,Nodes.Word,"T3","T2",Rels.Relevancy,"attr1")
# test.set_info("T1",Nodes.Word,'attr1:test1,attr2:test2')
# print(test.get_node("T1",Nodes.Word))
# test.del_node("10000001F1",Nodes.Favorite)
# print(test.has_word('T2'))
