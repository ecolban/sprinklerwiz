package org.wintrisstech.erik.sprinkler;

import java.io.IOException;
import java.io.PrintWriter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class ScheduleFormServlet extends HttpServlet {

	private static final String TIME_ERROR = "error";

	/**
	 * @param args
	 */

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		RequestDispatcher view = request.getRequestDispatcher("/view/form.jsp");
		response.setContentType("text/html");
		view.forward(request, response);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		Pattern timeParameterNamePattern = Pattern
				.compile("\\d\\d_time_(on|off)");

		Enumeration<String> parameterNames = request.getParameterNames();
		List<String> allParameters = new ArrayList<String>();
		while (parameterNames.hasMoreElements()) {
			String name = parameterNames.nextElement();
			allParameters.add(name);
		}
		boolean isError = false;
		for (String p : allParameters) {
			Matcher m = timeParameterNamePattern.matcher(p);
			if (m.matches()) {
				String time = request.getParameter(p);
				if (time != null && !time.isEmpty()
						&& validateTimeParameter(time) != null) {
					isError = true;
					break;
				}
			}
		}

		response.setContentType("text/plain");
		PrintWriter writer = response.getWriter();
		writer.println(isError ? TIME_ERROR : "This form has no errors.");
		Collections.sort(allParameters);
		for (String name : allParameters) {
			writer.print(name + " = ");
			writer.println(request.getParameter(name));
		}
		// RequestDispatcher view = request
		// .getRequestDispatcher("/view/edit.jsp");
		// view.forward(request, response);
	}

	private String validateTimeParameter(String parameter) {
		Pattern timePattern = Pattern.compile("(\\d?\\d):(\\d\\d)(:(\\d\\d))?");
		Matcher m = timePattern.matcher(parameter);
		if (m.matches()) {
			int hours = Integer.parseInt(m.group(1));
			int minutes = Integer.parseInt(m.group(2));
			int seconds = 0;

			if (m.group(4) != null) {
				seconds = Integer.parseInt(m.group(4));
			}
			if (0 <= hours && hours < 24 && 0 <= minutes && minutes < 60
					&& 0 <= seconds && seconds < 60) {
				return null;
			}
		}
		return TIME_ERROR;
	}
}
