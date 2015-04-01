package org.exoplatform.addons.web.filter;

import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.jcr.RepositoryService;
import org.exoplatform.services.jcr.ext.common.SessionProvider;
import org.exoplatform.web.filter.Filter;

import javax.jcr.Node;
import javax.jcr.Session;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by kmenzli on 27/03/15.
 */
public class PublicLinkFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest)request;
        HttpServletResponse httpResponse = (HttpServletResponse)response;

        String[] params = httpRequest.getRequestURI().split("/");
        RepositoryService repositoryService = (RepositoryService) PortalContainer.getInstance().getComponentInstanceOfType(RepositoryService.class);
        SessionProvider sessionProvider = SessionProvider.createSystemProvider();
        try
        {
            if (params.length==7)
            {
                String uuid = params[5];

                Session session = sessionProvider.getSession("collaboration", repositoryService.getCurrentRepository());

                Node node = session.getNodeByUUID(uuid);
                httpResponse.setContentType(getMimeType(node));
                httpResponse.setCharacterEncoding("UTF-8");
                httpResponse.setStatus(HttpServletResponse.SC_OK);
                InputStream in = getStream(node);
                OutputStream out = response.getOutputStream();
                byte[] buffer = new byte[1024];
                int len = in.read(buffer);
                while (len != -1) {
                    out.write(buffer, 0, len);
                    len = in.read(buffer);
                }

            }
        }
        catch (Exception e)
        {
            httpResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
        finally
        {
            sessionProvider.close();
        }
        return;

    }

    private InputStream getStream(Node node) throws Exception
    {
        if (node.hasNode("jcr:content")) {
            Node contentNode = node.getNode("jcr:content");
            if (contentNode.hasProperty("jcr:data")) {
                InputStream inputStream = contentNode.getProperty("jcr:data").getStream();
                return inputStream;
            }
        }
        return null;



    }

    private String getMimeType(Node node) throws Exception
    {
        if (node.hasNode("jcr:content")) {
            Node jcrContent = node.getNode("jcr:content");
            return jcrContent.getProperty("jcr:mimeType").getString();
        }
        return null;

    }
}
