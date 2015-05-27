package cn.wizzer.modules.file;

/**Ioc fileserver.json 配置文件服务器 服务端用户名密码
 * Created by Wizzer on 14-1-17.
 */
public class FileServer {
    private String username;
    private String password;
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
