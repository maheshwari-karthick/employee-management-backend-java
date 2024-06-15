//package com.employee_management.model;
//
//import lombok.Getter;
//import lombok.Setter;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import java.util.Arrays;
//import java.util.Collection;
//import java.util.List;
//import java.util.stream.Collectors;
//
//
//@Getter
//@Setter
//public class UserInformation implements UserDetails {
//    private final String name;
//    private final String password;
//    private final List<GrantedAuthority> authorities;
//
//    public UserInformation(User user) {
//        this.name = user.getUsername();
//        this.password = user.getPassword();
//        this.authorities = Arrays.stream(user.getRoles().toArray())
//                .map(role->new SimpleGrantedAuthority(String.valueOf(role)))
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return authorities;
//    }
//
//    @Override
//    public String getPassword() {
//        return password;
//    }
//
//    @Override
//    public String getUsername() {
//        return name;
//    }
//}
