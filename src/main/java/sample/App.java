package sample;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.beanutils.BeanUtils;
import org.eclipse.jetty.util.MultiMap;
import org.eclipse.jetty.util.UrlEncoded;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;

public class App {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        /**
         * 初期表示時です。
         */
        get("/", (Request req, Response res) -> {
            Map<String, Object> model = new HashMap<>();
            return new ModelAndView(model, "index");
        }, new ThymeleafTemplateEngine());

        /**
         * AjaxでJSONのやりとりを行います。 この場合formでのsubmitと画面遷移ができません。
         */
        post("/ajaxJSON", (Request req, Response res) -> {
            // モデルへの変換処理
            ObjectMapper mapper = new ObjectMapper();
            FormModel fm = mapper.readValue(req.queryParams("json"), FormModel.class);

            System.out.println("ajaxJSON to Model" + fm);
            String json = mapper.writeValueAsString(fm);
            return json;
        });

        /**
         * Requestのパラメータから値を取得してモデルに1つずつ詰めていきます。 パラメータが多い場合には煩雑かもしれません。
         */
        post("/fromParam", (Request req, Response res) -> {
            // モデルへの変換処理
            FormModel fm = new FormModel();
            fm.setId(req.queryParams("id"));
            fm.setText(req.queryParams("text"));

            System.out.println("fromParam to Model" + fm);
            return "fromParam to Model " + fm.toString();
        });

        /**
         * パラメータからMultiMapへ変換し、commons-beanutilsを利用してModelへ変換します。
         * ライブラリに制限がなければ汎用的に使えるこちらがおすすめです。
         */
        post("/bean2Model", (Request req, Response res) -> {
            FormModel fm = new FormModel();
            Map<String, Object> map = new HashMap<>();
            try {
                // モデルへの変換処理
                MultiMap<String> params = new MultiMap<>();
                UrlEncoded.decodeTo(req.body(), params, "UTF-8");
                BeanUtils.populate(fm, params);
                System.out.println(fm);
            } catch (Exception e) {
                halt(501);
                return null;
            }
            return "bean2Model " + fm.toString();
        });
    }
}
