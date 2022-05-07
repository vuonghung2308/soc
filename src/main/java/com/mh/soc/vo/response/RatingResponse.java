package com.mh.soc.vo.response;

import com.mh.soc.model.Rating;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class RatingResponse {
    private final Long id;
    private final Integer star;
    private final String comment;
    private final UserResponse user;

    public RatingResponse(Rating r) {
        id = r.getId();
        star = r.getStar();
        comment = r.getComment();
        user = new UserResponse(r.getUser());
    }

    public static List<RatingResponse> get(List<Rating> ratings) {
        ArrayList<RatingResponse> list = new ArrayList<>();
        for (Rating rating : ratings) {
            list.add(new RatingResponse(rating));
        }
        return list;
    }
}
