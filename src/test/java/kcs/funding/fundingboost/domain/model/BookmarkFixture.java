package kcs.funding.fundingboost.domain.model;

import kcs.funding.fundingboost.domain.entity.Bookmark;
import kcs.funding.fundingboost.domain.entity.Item;
import kcs.funding.fundingboost.domain.entity.member.Member;

public class BookmarkFixture {

    public static Bookmark bookmark1(Member member, Item item) throws NoSuchFieldException, IllegalAccessException {
        Bookmark bookmark = Bookmark.createBookmark(member, item);
//        Field favoriteId = bookmark.getClass().getDeclaredField("favoriteId");
//        favoriteId.setAccessible(true);
//        favoriteId.set(bookmark, 1L);

        return bookmark;
    }
}
