package volta.utils;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import volta.core.ModelAndView;
import volta.core.ViewParameter;

public class ModelAndViewHandler {
    public static void processModelAndView(ModelAndView mv, HttpServletRequest req, HttpServletResponse res) {
        HashMap<String, Object> data = mv.getAttribute();
        if (data != null) {
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                req.setAttribute(entry.getKey(), entry.getValue());
            }
        }

        String nomPage = ViewParameter.prefix
                + mv.getView()
                + ViewParameter.suffix;
        System.out.println(nomPage);
        try {
            RequestDispatcher dispatcher = req.getRequestDispatcher(nomPage);
            dispatcher.forward(req, res);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }

    }
}
