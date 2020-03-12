package wintervocationmajiangcommunity.controller;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import wintervocationmajiangcommunity.dto.AccessTokenDTO;
import wintervocationmajiangcommunity.dto.GithubUser;
import wintervocationmajiangcommunity.provide.GithubProvider;
import java.io.IOException;
@Controller
public class AuthorizeController {
    @Autowired
    private GithubProvider githubProvider;
    @Value("${github.client.id}")
    private  String  clientId;
    @Value("${github.client.secret}")
    private String clientSecret;
    @Value("${github.redirect.uri}")
    private String redirectUri;
    @GetMapping("/callback")
    public  String callback(@RequestParam(name = "code")String code,
                            @RequestParam(name="state")String state){
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setRedirect_uri(redirectUri);
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setState(state);
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);//ctrl+alt+v快速生成对象
        //shift+回车可迅速移动光标到下一行
        GithubUser user = githubProvider.getUser(accessToken);
        System.out.println(user.getId());
        System.out.println(user.getName());
        System.out.println(user.getBio());
        return "index";
    }
}
