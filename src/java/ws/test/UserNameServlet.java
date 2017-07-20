package ws.test;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "UserNameServlet", urlPatterns = {"/UserNameServlet"})
public class UserNameServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();
        HttpSession httpSession = request.getSession(true);
        String username = request.getParameter("username");
        httpSession.setAttribute("username", username);
        System.out.println("uns >> httpsess"+httpSession.getId());

        if (username != null) {
            pw.println("<html>");
            pw.println("<head>");
            pw.println("	<title>chat</title>");
            pw.println("</head>");
            pw.println("<body>");
            pw.println("<mark>username: " + username + "</mark><br/>");
            pw.println("<textarea id=\"messagesTextArea\" readonly=\"readonly\" rows=\"10\" cols=\"45\"></textarea>");
            pw.println("<input type=\"text\" id=\"messageText\" size=\"50\"/>");
            pw.println("<input type=\"button\" value=\"Send\" onclick=\"sendMessage();\"/>");
            pw.println("<script>");
            pw.println("	var websocket = new WebSocket(\"ws://localhost:8080/wst/chatroomServerEndpoint\");");
            pw.println("	websocket.onmessage = function processMessage(message){");
            pw.println("		var jsonData = JSON.parse(message.data);");
            pw.println("		if (jsonData.message != null) {");
            pw.println("			messagesTextArea.value += jsonData.message + \"\\n\";");
            pw.println("		}");
            pw.println("	}");
            pw.println("	function sendMessage(){");
            pw.println("		websocket.send(messageText.value);");
            pw.println("		messageText.value = \"\";");
            pw.println("	}");
            pw.println("</script>");
            pw.println("</body>");
            pw.println("</html>");

        }
    }

}
