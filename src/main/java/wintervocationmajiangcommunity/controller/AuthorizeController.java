package wintervocationmajiangcommunity.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import wintervocationmajiangcommunity.dto.AccessTokenDTO;
import wintervocationmajiangcommunity.dto.GithubUser;
import wintervocationmajiangcommunity.mapper.UserMapper;
import wintervocationmajiangcommunity.model.User;
import wintervocationmajiangcommunity.provide.GithubProvider;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
public class AuthorizeController {
    @Autowired
    private UserMapper userMapper;
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
                            @RequestParam(name="state")String state,
                            HttpServletRequest request,
                            HttpServletResponse response){
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setRedirect_uri(redirectUri);
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setState(state);
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);//ctrl+alt+v快速生成对象
        //shift+回车可迅速移动光标到下一行
        GithubUser githubUser = githubProvider.getUser(accessToken);
        if(githubUser!=null){
            User user = new User();
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setName(githubUser.getName());
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
            //登录成功，写cookie和Session
            response.addCookie(new Cookie("token",token));
//            request.getSession().setAttribute("user",githubUser);
            return  "redirect:/";
        }else{
            return  "redirect:/";
        }
    }
}
