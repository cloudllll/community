package wintervocationmajiangcommunity.mapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import wintervocationmajiangcommunity.model.User;
//ctrl+alt+o自动移除无用的包
@Mapper
public interface UserMapper {
    @Insert("Insert into USER(name,account_id,token,gmt_create,gmt_modified)" +
            "values(#{name},#{accountId},#{token},#{gmtCreate},#{gmtModified})")
    void insert(User user);
    @Select("select * from USER where token=#{token}")
    User findByToken(@Param("token") String token);//当参数不是类对象的时候
}
