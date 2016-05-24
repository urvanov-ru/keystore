package ru.urvanov.keystore.api.controller;

import java.io.IOException;
import java.io.Serializable;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ru.urvanov.keystore.annotation.CurrentUser;
import ru.urvanov.keystore.domain.ClientUserDetailsImpl;
import ru.urvanov.keystore.domain.Key;
import ru.urvanov.keystore.domain.KeyStatus;
import ru.urvanov.keystore.service.KeyService;

@Controller
public class ApiController {
    private static final Logger logger = LoggerFactory
            .getLogger(ApiController.class);

    @Autowired
    private KeyService keyService;

    // @Autowired
    // private MessageSource messageSource;

    @RequestMapping(value = "/key/list", method = RequestMethod.GET)
    public @ResponseBody KeyListResponse list(
            @CurrentUser ClientUserDetailsImpl clientUserDetailsImpl,
            HttpServletResponse httpServletResponse) throws IOException {
        try {
            logger.info("/key/list started.");
            KeyListResponse result = new KeyListResponse();
            Long clientId = clientUserDetailsImpl.getClient().getId();
            List<Key> keyList = keyService.findByClientId(clientId);
            for (Key key : keyList) {
                KeyListItem keyListItem = new KeyListItem();
                keyListItem.setId(String.valueOf(key.getId()));
                keyListItem.setDateBegin(key.getDateBegin());
                keyListItem.setDateEnd(key.getDateEnd());
                keyListItem.setStatus(key.getStatus());
                result.getKeys().add(keyListItem);
            }
            logger.info("/key/list finished.");
            return result;
        } catch (Exception ex) {
            logger.error("/key/list failed.", ex);
            httpServletResponse
                    .sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return null;
        }
    }

    public static class KeyListResponse implements Serializable {

        /**
         * 
         */
        private static final long serialVersionUID = -6702016664280175007L;

        private List<KeyListItem> keys = new ArrayList<KeyListItem>();

        public List<KeyListItem> getKeys() {
            return keys;
        }

        public void setKeys(List<KeyListItem> keys) {
            this.keys = keys;
        }

    }

    public static class KeyListItem implements Serializable {
        /**
         * 
         */
        private static final long serialVersionUID = 2779909462713179139L;
        private String id;
        private Date dateBegin;
        private Date dateEnd;
        private KeyStatus status;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Date getDateBegin() {
            return dateBegin;
        }

        public void setDateBegin(Date dateBegin) {
            this.dateBegin = dateBegin;
        }

        public Date getDateEnd() {
            return dateEnd;
        }

        public void setDateEnd(Date dateEnd) {
            this.dateEnd = dateEnd;
        }

        public KeyStatus getStatus() {
            return status;
        }

        public void setStatus(KeyStatus status) {
            this.status = status;
        }

        @Override
        public String toString() {
            return "KeyListItem id=" + id + ", dateBegin=" + dateBegin
                    + ", dateEnd=" + dateEnd + ", status=" + status;
        }
    }

    @RequestMapping(value = "/key", method = RequestMethod.GET, produces = "application/octet-stream")
    public @ResponseBody byte[] key(@RequestParam(value = "id") Long id,
            HttpServletResponse httpServletResponse,
            @CurrentUser ClientUserDetailsImpl clientUserDetailsImpl)
            throws IOException {
        try {
            logger.info("/key/key started.");
            Key key = keyService.findById(id);
            if (!clientUserDetailsImpl.getClient().getId()
                    .equals(key.getClient().getId())) {
                httpServletResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
                return null;
            }
            // httpServletResponse.setContentType("application/octet-stream");
            String fileName = "file.key";
            logger.info("fileName=" + fileName);
            httpServletResponse.setHeader(
                    "Content-Disposition",
                    "attachment; filename*=UTF-8''"
                            + URLEncoder.encode(fileName, "UTF-8") + "");
            logger.info("/key/key finished.");
            return key.getCode().getBytes("UTF-8");
        } catch (Exception ex) {
            logger.error("/key/failed.", ex);
            httpServletResponse
                    .sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return null;
        }
    }
}
