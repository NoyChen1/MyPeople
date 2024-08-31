    package com.example.mystore.Network;

    import com.example.mystore.Models.UserResponse;
    import com.example.mystore.Models.UsersData;

    import retrofit2.Call;
    import retrofit2.http.GET;
    import retrofit2.http.Path;
    import retrofit2.http.Query;

    public interface Client {

        @GET("/api/users/{uid}")
        Call<UserResponse> getUser(@Path("uid") String uid);

        @GET("/api/users")
        Call<UsersData> getUsers(@Query("page") int page);
    }
