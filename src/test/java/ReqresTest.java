import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;


public class ReqresTest {

    private final static String URL = "https://reqres.in";

    private void getSpecificationResponse(ResponseSpecification response) {
        Specifications.installSpecification(Specifications.requestSpecification(URL),
                response);
    }

    private List<UserData> getUserData() {
        getSpecificationResponse(Specifications.responseSpecification200());
        List<UserData> users = given()
                .when()
                .get("/api/users?page=2")
                .then()
                .log()
                .all()
                .extract()
                .body()
                .jsonPath()
                .getList("data", UserData.class);
        return users;
    }


    @Test
    public void checkAvatarAndIdTest() {
        List<UserData> users = getUserData();
        users.forEach(x -> Assertions.assertTrue(x.getAvatar().contains(x.getId().toString())));
    }
//        Another way to compare that avatars contains ids
//        List<String> avatars = users.stream().map(UserData::getAvatar).collect(Collectors.toList());
//        List<String> ids = users.stream().map(x -> x.getId().toString()).collect(Collectors.toList());
//        for (int i = 0; i < avatars.size(); i++) {
//            Assertions.assertTrue(avatars.get(i).contains(ids.get(i)));
//        }


    @Test
    public void checkEmailEndsTest() {
        List<UserData> users = getUserData();
        Assertions.assertTrue(users.stream().allMatch(x -> x.getEmail().endsWith("@reqres.in")));
    }

    @Test
    public void successfulRegistrationTest() {
        getSpecificationResponse(Specifications.responseSpecification200());
        Integer id = 4;
        String token = "QpwL5tke4Pnpja7X4";
        Register user = new Register("eve.holt@reqres.in", "pistol");
        SuccessRegister successRegister = given()
                .body(user)
                .when()
                .post("/api/register")
                .then()
                .log()
                .all()
                .extract()
                .as(SuccessRegister.class);
        Assertions.assertNotNull(successRegister.getId());
        Assertions.assertNotNull(successRegister.getToken());
        Assertions.assertEquals(id, successRegister.getId());
        Assertions.assertEquals(token, successRegister.getToken());
    }

    @Test
    public void unsuccessfulRegistrationTest() {
        getSpecificationResponse(Specifications.responseSpecification400());
        Register user = new Register("sydney@fife", "");
        UnsuccessRegister unsuccessRegister = given()
                .body(user)
                .when()
                .post("/api/register")
                .then()
                .log()
                .all()
                .extract()
                .as(UnsuccessRegister.class);
        Assertions.assertEquals("Missing password", unsuccessRegister.getError());
    }

    @Test
    public void sortedByYearsTest() {
        getSpecificationResponse(Specifications.responseSpecification200());
        List<ColorData> colors = given()
                .when()
                .get("/api/unknown")
                .then()
                .log()
                .all()
                .extract()
                .body()
                .jsonPath()
                .getList("data", ColorData.class);
        List<Integer> years = colors.stream().map(ColorData::getYear).collect(Collectors.toList());
        List<Integer> sortedYears = years.stream().sorted().collect(Collectors.toList());
        Assertions.assertEquals(sortedYears, years);
    }
}
