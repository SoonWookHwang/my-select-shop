package com.sparta.springnew.mvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.springnew.controller.ProductController;
import com.sparta.springnew.controller.UserController;
import com.sparta.springnew.dto.ProductRequestDto;
import com.sparta.springnew.model.User;
import com.sparta.springnew.model.UserRoleEnum;
import com.sparta.springnew.security.UserDetailsImpl;
import com.sparta.springnew.security.WebSecurityConfig;
import com.sparta.springnew.service.KakaoUserService;
import com.sparta.springnew.service.ProductService;
import com.sparta.springnew.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(
        controllers = {UserController.class, ProductController.class},
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = WebSecurityConfig.class
                )
        }
)
class UserProductMvcTest {
    private MockMvc mvc;

    private Principal mockPrincipal;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    UserService userService;

    @MockBean
    KakaoUserService kakaoUserService;

    @MockBean
    ProductService productService;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity(new MockSpringSecurityFilter()))
                .build();
    }

    private void mockUserSetup() {
// Mock ????????? ?????? ??????
        String username = "?????????";
        String password = "hope!@#";
        String email = "hope@sparta.com";
        UserRoleEnum role = UserRoleEnum.USER;
        User testUser = new User(username, password, email, role);
        UserDetailsImpl testUserDetails = new UserDetailsImpl(testUser);
        mockPrincipal = new UsernamePasswordAuthenticationToken(testUserDetails, "", testUserDetails.getAuthorities());
    }

    @Test
    @DisplayName("????????? view")
    void test1() throws Exception {
// when - then
        mvc.perform(get("/user/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andDo(print());
    }

    @Test
    @DisplayName("?????? ?????? ?????? ??????")
    void test2() throws Exception {
// given
        MultiValueMap<String, String> signupRequestForm = new LinkedMultiValueMap<>();
        signupRequestForm.add("username", "?????????");
        signupRequestForm.add("password", "hope!@#");
        signupRequestForm.add("email", "hope@sparta.com");
        signupRequestForm.add("admin", "false");

// when - then
        mvc.perform(post("/user/signup")
                        .params(signupRequestForm)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/user/login"))
                .andDo(print());
    }

    @Test
    @DisplayName("?????? ???????????? ??????")
    void test3() throws Exception {
// given
        this.mockUserSetup();
        String title = "Apple <b>?????????</b> 2?????? ???????????? ?????? (MV7N2KH/A)";
        String imageUrl = "https://shopping-phinf.pstatic.net/main_1862208/18622086330.20200831140839.jpg";
        String linkUrl = "https://search.shopping.naver.com/gate.nhn?id=18622086330";
        int lPrice = 77000;
        ProductRequestDto requestDto = new ProductRequestDto(
                title,
                imageUrl,
                linkUrl,
                lPrice
        );

        String postInfo = objectMapper.writeValueAsString(requestDto);
        //opjectMapper = json ????????? ????????????

// when - then
        mvc.perform(post("/api/products")
                        .content(postInfo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }
}
