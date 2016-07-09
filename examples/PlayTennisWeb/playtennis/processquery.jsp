
<%@ page import="jcolibri.cbrcore.*" %>
<%@ page import="jcolibri.cbrcore.Exception.*" %>
<%@ page import="jcolibri.cbrcase.*" %>
<%@ page import="jcolibri.similarity.*" %>
<%@ page import="jcolibri.util.*" %>
<%@ page import="jcolibri.extensions.web.bridge.*" %>

<%@ page import="java.util.*" %>

<html>
<head></head>
<body>
<table border="0" cellpadding="2" cellspacing="7" width="100%">

  <tr>
  <td>
	<a href="http://gaia.fdi.ucm.es"><img src="gaia.gif" border="0"></a>
  </td>
  <td colspan="2" valign="middle" bgcolor="#738EAB">
    <font size="-1">
       <h1><font face="arial" color="#ffffff"> GAIA - Group for Artificial Intelligence Applications</font></h1>
	</font>

	<hr color="White">
  </td>
  </tr>
</table>

<%
     try{
        String month = request.getParameter("month");
        Integer last_match = new Integer(request.getParameter("lastmatch"));
        Boolean is_rainning = new Boolean(request.getParameter("israinning"));

	out.println("Query:<p>");
	out.println("month: "+ month);
	out.println("<br>last match: "+last_match);
	out.println("<br>is rainning: "+is_rainning);
        
        HashMap qv = new HashMap();
        qv.put("Rainning", is_rainning);
        qv.put("Month", month);
        qv.put("LastMatch", last_match);
        
        try
        {
           Synchronizer.webSessionAdquieresCycle(session.getId());
           WebBridge.putData("QUERY_VALUES",qv);
           Synchronizer.webSessionReleasesTurn(session.getId());
           Synchronizer.webSessionWaitsTurn(session.getId());
           
   	   out.println("<p>Result:</p>");

	   List cases = (List)WebBridge.getData("RETRIEVED_CASES");
	   for(Iterator iter = cases.iterator();iter.hasNext();)
	   {
		CBRCase c = (CBRCase)iter.next();
		out.println(c.toString().replaceAll("\n","<BR>"));
		out.println("<p>");
	   }
           
        }catch(SessionOutException soe)
        {
		out.println("<p>CBRCycle busy or Timeout. Try again</p>");
        }
        
     }
     catch(Exception e){
        out.println("<p>Error. Try again.<br>");
        out.println(e.getMessage()+"<br>");
        e.printStackTrace();
        out.println("<p>");
     }

%>   

<a href="playtennis.html">Back</a>
</body>
</html>
