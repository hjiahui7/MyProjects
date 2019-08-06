package Servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import K.VSave.*;
import Services.*;

@SuppressWarnings("serial")
public class Servlet extends HttpServlet {

	// two URI
	public static final String IServiceURI = "/IndexService";
	public static final String PublishURI = "/Publish";
	public static final String LikeServiceURI = "/LikeService";
	public static final String DislikeServiceURI = "/DislikeService";
	public static final String CommentServiceURI = "/CommentService";
	// Data
	LKVStoreInterface<String, ServiceInterface> KVStore;

	public Servlet() {
		System.out.println("servlet≥ı ºªØ°≠°≠");
		initialServlet();
	}

	public void initialServlet() {
		KVStore = new LKVStore<String, ServiceInterface>();
		KVStore.addFirst(IServiceURI, new IndexService());
		KVStore.addFirst(PublishURI, new PublishService());
		KVStore.addFirst(LikeServiceURI, new LikeService());
		KVStore.addFirst(DislikeServiceURI, new DislikeService());
		KVStore.addFirst(CommentServiceURI, new CommentService());
	}

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		String requestServlet = req.getRequestURI().toString();
		String path = requestServlet.substring(req.getContextPath().length());
		String header = req.getHeader("referer");
		if (header == null) {
			path = IServiceURI;
		}
		String web = null;
		try {
			web = KVStore.get(path).makeWeb(req);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		PrintWriter out = response.getWriter();
		if (web != null) {
			out.print(web);
		}
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doGet(req, res);
	}

	@Override
	public void destroy() {
		System.out.println("servletœ˙ªŸ£°");
		super.destroy();
	}
}
