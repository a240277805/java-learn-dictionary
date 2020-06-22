package com.zmk.github.test.数据结构;

public class Tree {
    private Node root=null;
    //查找节点
    public Node find(int key) {
        Node current = root;
        while(current != null){
            if(current.getData() > key){//当前值比查找值大，搜索左子树
                current = current.getLeftChild();
            }else if(current.data < key){//当前值比查找值小，搜索右子树
                current = current.rightChild;
            }else{
                return current;
            }
        }
        return null;//遍历完整个树没找到，返回null
    }

    //插入节点
    public boolean insert(int data) {
        Node newNode = new Node(data);
        if(root == null){//当前树为空树，没有任何节点
            root = newNode;
            return true;
        }else{
            Node current = root;
            Node parentNode = null;
            while(current != null){
                parentNode = current;
                if(current.data > data){//当前值比插入值大，搜索左子节点
                    current = current.leftChild;
                    if(current == null){//左子节点为空，直接将新值插入到该节点
                        parentNode.leftChild = newNode;
                        return true;
                    }
                }else{
                    current = current.rightChild;
                    if(current == null){//右子节点为空，直接将新值插入到该节点
                        parentNode.rightChild = newNode;
                        return true;
                    }
                }
            }
        }
        return false;
    }

    //中序遍历
    public void infixOrder(Node current){
        if(current != null){
            infixOrder(current.leftChild);
            System.out.print(current.data+" ");
            infixOrder(current.rightChild);
        }
    }

    //前序遍历
    public void preOrder(Node current){
        if(current != null){
            System.out.print(current.data+" ");
            preOrder(current.leftChild);
            preOrder(current.rightChild);
        }
    }

    //后序遍历
    public void postOrder(Node current){
        if(current != null){
            postOrder(current.leftChild);
            postOrder(current.rightChild);
            System.out.print(current.data+" ");
        }
    }


    class Node {
        private int data;    //节点数据
        private Node leftChild; //左子节点的引用
        private Node rightChild; //右子节点的引用

        public Node(int data){
            this.data=data;
        }
        //打印节点内容
        public void display(){
            System.out.println(data);
        }


        public int getData() {
            return data;
        }

        public void setData(int data) {
            this.data = data;
        }

        public Node getLeftChild() {
            return leftChild;
        }

        public void setLeftChild(Node leftChild) {
            this.leftChild = leftChild;
        }

        public Node getRightChild() {
            return rightChild;
        }

        public void setRightChild(Node rightChild) {
            this.rightChild = rightChild;
        }
    }
}
