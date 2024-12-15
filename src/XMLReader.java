import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NamedNodeMap;

//XML File 읽어서 파싱하는 클래스
public class XMLReader {
    private Document XMLDoc;

    public final String E_SETTING= "Setting";
    public final String E_HOWMANYROWS="HowManyRows"; //몇 행으로 할지
    public final String E_HOWMANYCOLS="HowManyCols"; // 몇 열로 할지
    public final String E_IMAGE = "Image";
    public final String E_BLOCKIMAGE_1="BlockImagePath_1";
    public final String E_BLOCKIMAGE_2="BlockImagePath_2";
    public final String E_OBSTACLEIMAGE="ObstacleImagePath";
    private Node settingElement=null;
    private Node imageElement = null;
    private Node blockImageElement=null;
    private Node obstacleImageElement=null;
    private Node howManyRowElement=null;
    private Node howManyColElement=null;
    public XMLReader(String XMLFile) {
        read(XMLFile); // XMLFile을 읽어 파싱하고 XMLDoc를 생성 XMLDOC은 멤버
        process(XMLDoc);
    }

    // XMLFile을 읽고 파싱하여 XMLDoc 객체 생성
    // XMLDOC 객체가 있어야 트리구조가 되고, 계층 접근이 쉽다
    private void read(String XMLFile) {
        DocumentBuilderFactory factory=null;
        DocumentBuilder builder=null;

        factory = DocumentBuilderFactory.newInstance();
        factory.setIgnoringComments(false);
        factory.setIgnoringElementContentWhitespace(false);
        try {
            builder = factory.newDocumentBuilder();
            File f = new File(XMLFile);
            XMLDoc = builder.parse(f);
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    public void process(Node parentNode) {
        //부모노드 (<GameConfig>)부터 시작해서 끝까지 계속 루프
        for (Node node = parentNode.getFirstChild(); node!= null; node = node.getNextSibling()) {
            //부모노드면 그냥 지나가기
            if(node.getNodeType() != Node.ELEMENT_NODE)
                continue;
            //여기부터 각각 노드 값 가져와서 설정
            else if(node.getNodeName().equals(E_SETTING)){
                settingElement = node;
            }
            else if(node.getNodeName().equals(E_IMAGE)){
                imageElement = node;
            }
//            else if(node.getNodeName().equals(E_BLOCKIMAGE)){
//                blockImageElement=node;
//            }
//            else if(node.getNodeName().equals(E_OBSTACLEIMAGE)){
//                obstacleImageElement=node;
//            }
            //attr 방식 말고 <HowManyRows>8</HowManyRows> 이렇게 하고싶을까봐 일단 주석처리
//            else if(node.getNodeName().equals(E_SIZE)){
//                sizeElement=node;
//            }
//            else if(node.getNodeName().equals(E_HOWMANYROWS)){
//                howManyRowElement=node;
//            }
//            else if(node.getNodeName().equals(E_HOWMANYCOLS)){
//                howManyColElement=node;
//            }
            process(node);
        }
    }
    //나중에 node 자체 얻어올 일 있을까봐 추가
    public Node getNode(Node parentNode, String nodeName) {
        Node node = null;
        for (node = parentNode.getFirstChild(); node != null;
             node = node.getNextSibling()) {
            if(node.getNodeType() != Node.ELEMENT_NODE)
                continue;
            if(node.getNodeName().equals(nodeName))
                return node;
            else {
                Node n = getNode(node, nodeName);
                if(n != null)
                    return n;
            }
        }
        return node;
    }

    public String getAttr(Node element, String attrName)
    {
        NamedNodeMap attrs = element.getAttributes();
        for(int i=0; i<attrs.getLength(); i++) {
            Node attr = attrs.item(i);
            String name = attr.getNodeName();
            if(name.equals(attrName)) {
                return attr.getNodeValue();
            }
        }

        return null;
    }

    public Node getSettingElement() {
        return settingElement;
    }
    public Node getImageElement(){return imageElement;}
    public Node getBlockImageElement(){return blockImageElement;}

    public Node getObstacleImageElement() {return obstacleImageElement;}
}