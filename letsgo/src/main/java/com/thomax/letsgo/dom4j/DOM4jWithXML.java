package com.thomax.letsgo.dom4j;

import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class DOM4jWithXML {

	public static void main(String[] args) throws Exception {
		//读XML文件内容
		DOM4jWithXML dWXML = new DOM4jWithXML();
		List<Student> students = dWXML.readXMLFromIO("students.xml");
		for (Student student : students) {
			System.out.println(student);
		}
		
		//写入到新的XML文件
		dWXML.writeToXML(students);
	}
	
	public List<Student> readXMLFromIO(String nameXML) throws Exception {
		List<Student> students = new ArrayList<Student>();
		SAXReader reader = new SAXReader();
		String packagePath = this.getClass().getResource(".").getPath();
		InputStream is = new FileInputStream(new File(packagePath + nameXML));  //获得系统资源文件
		Document document = reader.read(is);
		Element root = document.getRootElement();
		@SuppressWarnings("unchecked")
        List<Element> elements = (List<Element>)root.elements();
		
		for (Element e : elements) {
			String sid = e.attribute("sid").getText();
			String sname = e.elementTextTrim("sname");
			String sex = e.elementTextTrim("sex");
			String age = e.elementTextTrim("age");
			String addr = e.elementTextTrim("addr");
			Student student = new Student(new Integer(sid), sname, sex, new Integer(age), addr);
			students.add(student);
		}
		
		return students;
	}
	
	public void writeToXML(List<Student> students) throws Exception {
		DocumentFactory factory = DocumentFactory.getInstance();
		Document document = factory.createDocument();
		Element root = document.addElement("students");
		
		for (Student student : students) {
			Element studentE = root.addElement("student");
			studentE.addAttribute("sid", student.getSid() + "");
			studentE.addElement("sname", student.getSname());
			studentE.addElement("sex", student.getSex());
			studentE.addElement("age", student.getAge() + "");
			studentE.addElement("addr", student.getAddr());
		}
		
		String path = System.getProperty("user.dir") + "/schematic/";
		Writer out = new FileWriter(new File(path + "studnts_bak.xml"));
		OutputFormat format = OutputFormat.createPrettyPrint(); //整齐排列XML元素，类似SimpleDateFormat
		XMLWriter writer = new XMLWriter(out, format);
		writer.write(document);
		writer.close();
		System.out.println("\nXML文件写入完毕！！");
	}

}








