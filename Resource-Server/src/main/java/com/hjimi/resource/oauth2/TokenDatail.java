package com.hjimi.resource.oauth2;

import com.it.uaa.common.domain.Oauth2Type;
import lombok.Builder;
import lombok.Data;

/**
 * @Auther: lxr
 * @Date: 2018/10/9 10:19
 * @Description:
 */

@Data
@Builder
public class TokenDatail {

    private String token;

    private Oauth2Type oauth2Type;

    private String resourceId;

    private String userId;




}
