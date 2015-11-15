/**
 * The MIT License
 *
 * Copyright (c) 2015, Sebastian Sdorra
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.github.sdorra.dto;

import com.github.sdorra.dto.api.RestApi;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.jboss.resteasy.plugins.server.servlet.HttpServletDispatcher;

/**
 *
 * @author Sebastian Sdorra
 */
public class App
{

  public static void main(String[] args) throws Exception
  {
    addDemoData();
    startServer();
  }
  
  private static void addDemoData(){
    User adams = new User("adams", "Douglas", "Adams");
    User dent = new User("dent", "Arthur", "Dent");
    User tricia = new User("tricia", "Trillian", "McMillian");
    User prefect = new User("prefect", "Ford", "Prefect");
    UserService.getInstance().add(adams, dent, tricia, prefect);
    
    GroupService.getInstance().add(new Group("hitchhiker", "The hitchhicker group", adams, dent, tricia, prefect));
    GroupService.getInstance().add(new Group("autors", "Group of authors", adams));
  }
  
  private static void startServer() throws Exception {
    Server server = new Server(8080);
    ServletContextHandler servletHandler = new ServletContextHandler();

    servletHandler.addServlet(DefaultServlet.class, "/*");
    servletHandler.addServlet(new ServletHolder(HttpServletDispatcher.class), "/v1/*");

    servletHandler.setInitParameter("resteasy.servlet.mapping.prefix", "/v1");
    servletHandler.setInitParameter("javax.ws.rs.Application", RestApi.class.getName());

    server.setHandler(servletHandler);
    server.start();
    server.join();
  }
}
