package wjy.yo.ereader.remotevo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserInfo extends OpResult {

    private boolean login;

    private String name;

    private String nickName;

    private String accessToken;
}
