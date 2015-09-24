package com.animals.app.repository;

import com.animals.app.domain.UserRole;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by oleg on 24.07.2015.
 */
public interface UserRoleRepository {

    final String SELECT_BY_ID = "SELECT Id, UserRole FROM userroles WHERE Id = #{id};";
    final String SELECT_ALL = "SELECT Id, UserRole FROM userroles;";

    /**
     * Returns a User role instance from the database.
     * @param id primary key value used for lookup.
     * @return A User role instance with a primary key value equals to pk. null if there is no matching row.
     */
    @Select(SELECT_BY_ID)
    @Results(value = {
            @Result(property="id", column="Id"),
            @Result(property="role", column="UserRole")
    })
    UserRole getById(int id);

    /**
     * Returns the list of all User roles instances from the database.
     * @return the list of all User roles instances from the database.
     */
    @Select(SELECT_ALL)
    @Results(value = {
            @Result(property="id", column="Id"),
            @Result(property="role", column="UserRole")
    })
    List<UserRole> getAll();
}
