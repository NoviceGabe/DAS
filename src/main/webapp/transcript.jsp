<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="com.das.model.UserModel" %>
<%@ page import="com.das.model.AppointmentModel" %>
<%@ page import="com.das.dao.AppointmentDao" %>
<%@ page import=" com.das.helper.AppointmentColumn" %>
<%@ page import="com.das.helper.UploadData" %>
<%@ page import="org.apache.pdfbox.pdmodel.*" %>
<%@ page import="org.apache.pdfbox.pdmodel.font.PDType1Font" %>
<%@ page import="org.json.simple.JSONObject" %>
<%@ page import="java.io.File" %>
<%@ page import="java.io.FileWriter" %>
<%@ page import="java.io.IOException" %>
<%@ page import="java.util.ArrayList" %>

 <%	
	UserModel user = (UserModel) session.getAttribute("user");
	if(user == null){
		response.sendRedirect("index.jsp");
	}

	String filename = "transcript.pdf";
	String path = "C:/DAS/download/";
	boolean status = false;
	String message = "Unable to download transcript";
	
	String name = "";
	String contact = "";
	String address = "";
	
	int appoId = 0;
	int userId = 0;
	try{
		appoId = Integer.parseInt(request.getParameter("appoId"));
		userId = Integer.parseInt(request.getParameter("userId"));
		
		ArrayList<UploadData<AppointmentColumn, ?>> data = new ArrayList<UploadData<AppointmentColumn, ?>>();
		data.add(new UploadData<AppointmentColumn, Integer>(new AppointmentColumn().id(), appoId));
		data.add(new UploadData<AppointmentColumn, Integer>(new AppointmentColumn().patient_id(), userId));
		AppointmentDao appoDao = new AppointmentDao("APPOINTMENTS");
		
		ArrayList<AppointmentModel> appos = appoDao.get(data);
		
		if(user.getId() != userId || appos.size() == 0){
			throw new Exception("Unable to download appointment");
		}
		
		AppointmentModel appoModel = new AppointmentModel();
		appoModel = appos.get(0);
		name = user.getName();
		contact = user.getContact();
		address = user.getAddress();
	}catch(Exception e){
		message = e.getMessage();
	}
	
	try {
	      FileWriter myWriter = new FileWriter("C:/test.txt");
	      String str = "appoid: "+appoId + " userid:" +userId;
	      myWriter.write(str);
	      myWriter.close();
	      System.out.println("Successfully wrote to the file.");
	    } catch (IOException e) {
	      System.out.println("An error occurred.");
	      e.printStackTrace();
	    }
	
	
	File dir = new File(path);
	dir.mkdirs();
	
	 try (PDDocument doc = new PDDocument()) {

         PDPage myPage = new PDPage();
         doc.addPage(myPage);

         try (PDPageContentStream cont = new PDPageContentStream(doc, myPage)) {

             cont.beginText();

             cont.setFont(PDType1Font.TIMES_ROMAN, 12);
             cont.setLeading(14.5f);

             cont.newLineAtOffset(25, 700);
             String line1 = "MINDTECH HOSPITAL";
             cont.showText(line1);

             cont.newLine();

             String line2 = "Patient Details";
                     
             cont.showText(line2);
             cont.newLine();
             
             String line3 = "Name: " + name;
             
             cont.showText(line3);
             cont.newLine();
             
 			 String line4 = "Contact: " + contact;
             
             cont.showText(line4);
             cont.newLine();
             
 			String line5 = "Address: " + address;
             
             cont.showText(line5);
             cont.newLine();

             String line6 = "Appointment Details";
             cont.showText(line6);
             cont.newLine();


             cont.endText();
             message = "Transcript has been downloaded located in " + dir;
             status = true;
         }catch(Exception e){
        	 message = e.getMessage();
         }
         doc.save(path+filename);
         
                
        response.setContentType("application/json");
 		JSONObject jsonObject = new JSONObject();
 		jsonObject.put("status", status);
 		jsonObject.put("message", message);
 		
 		response.getWriter().write(jsonObject.toString());
     }

%>