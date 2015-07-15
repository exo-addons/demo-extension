package org.exoplatform.addons.uiexplorer.extention.webui.component;

import org.exoplatform.ecm.webui.component.explorer.UIJCRExplorer;
import org.exoplatform.ecm.webui.component.explorer.control.filter.*;
import org.exoplatform.ecm.webui.component.explorer.control.listener.UIActionBarActionListener;
import org.exoplatform.services.jcr.RepositoryService;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.services.pdfviewer.PDFViewerService;
import org.exoplatform.services.wcm.utils.WCMCoreUtils;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.config.annotation.EventConfig;
import org.exoplatform.webui.core.UIComponent;
import org.exoplatform.webui.core.UIPopupContainer;
import org.exoplatform.webui.event.Event;
import org.exoplatform.webui.ext.filter.UIExtensionFilter;
import org.exoplatform.webui.ext.filter.UIExtensionFilters;

import javax.jcr.Node;
import javax.print.*;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

/**
 * Created by kmenzli on 18/03/15.
 */
@ComponentConfig(
        events = {
                @EventConfig(listeners = PrintDocumentActionComponent.PrintDocumentActionListener.class)
        }
)
public class PrintDocumentActionComponent extends UIComponent {

    private static final Log LOG  = ExoLogger.getLogger(PrintDocumentActionComponent.class.getName());

    private static final List<UIExtensionFilter> FILTERS = Arrays.asList(new UIExtensionFilter[]{
            new IsNotRootNodeFilter("UIActionBar.msg.cannot-enable-version-rootnode"),
            new CanSetPropertyFilter(), new IsNotLockedFilter(), new CanEnableVersionFilter(),
            new IsNotEditingDocumentFilter(), new IsNotFolderFilter(), new IsNotIgnoreVersionNodeFilter()
    });

    @UIExtensionFilters
    public List<UIExtensionFilter> getFilters() {
        return FILTERS;
    }

    public static class PrintDocumentActionListener extends UIActionBarActionListener<PrintDocumentActionComponent> {
        protected void processEvent(Event<PrintDocumentActionComponent> event) throws Exception {
            UIJCRExplorer uiExplorer = event.getSource().getAncestorOfType(UIJCRExplorer.class);
            UIPopupContainer UIPopupContainer = uiExplorer.getChild(UIPopupContainer.class);
            Node currentNode = uiExplorer.getCurrentNode();
            uiExplorer.setIsHidePopup(false);
            try {

                printJCRDocument(currentNode);
                LOG.info("## The Document ["+currentNode.getName()+"] has been successfully printed ");

            } catch (Exception E) {
                LOG.error("## Error during sending the document ["+currentNode.getName()+"] to the printer");

            }
            event.getRequestContext().addUIComponentToUpdateByAjax(UIPopupContainer);

        }
    }

    private static void printJCRDocument (Node theNode) throws Exception {

        PDFViewerService pdfViewerService = WCMCoreUtils.getService(PDFViewerService.class);

        RepositoryService repositoryService = WCMCoreUtils.getService(RepositoryService.class);

        String repoName = repositoryService.getCurrentRepository().getConfiguration().getName();

        File file = pdfViewerService.getPDFDocumentFile(theNode, repoName);

        //String defaultPrinter = PrintServiceLookup.lookupDefaultPrintService().getName();

        PrintService service = PrintServiceLookup.lookupDefaultPrintService();

        FileInputStream in = new FileInputStream(file);

        PrintRequestAttributeSet  pras = new HashPrintRequestAttributeSet();

        pras.add(new Copies(1));

        DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;

        Doc doc = new SimpleDoc(in, flavor, null);

        DocPrintJob job = service.createPrintJob();

        job.print(doc, pras);

        in.close();

        // send FF to eject the page
        InputStream ff = new ByteArrayInputStream("\f".getBytes());

        Doc docff = new SimpleDoc(ff, flavor, null);

        DocPrintJob jobff = service.createPrintJob();

        jobff.print(docff, null);

    }
}
