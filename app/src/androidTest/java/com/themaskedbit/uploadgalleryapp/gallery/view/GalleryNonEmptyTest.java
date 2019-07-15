package com.themaskedbit.uploadgalleryapp.gallery.view;

import android.view.View;
import android.widget.ImageView;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.themaskedbit.uploadgalleryapp.R;
import com.themaskedbit.uploadgalleryapp.gallery.model.TestUser;
import com.themaskedbit.uploadgalleryapp.gallery.model.User;

import org.hamcrest.Description;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static org.hamcrest.core.AllOf.allOf;

@RunWith(AndroidJUnit4.class)
public class GalleryNonEmptyTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(
                    MainActivity.class, false, false);

    private IdlingResource mIdlingResource;


    @Before
    public void setup() {
        TestUser.set("user1");
    }

    @Test
    public void testFullGallery() {
        launchActivity();
        onView(allOf(withId(R.id.gallery_rv),isDescendantOfA(allOf(withId(R.id.images_fragment), isDescendantOfA(withId(R.id.layout_gallery)))))).check(matches(isDisplayed()));

        onView(allOf(withId(R.id.gallery_rv),isDescendantOfA(allOf(withId(R.id.images_fragment), isDescendantOfA(withId(R.id.layout_gallery)))))).check(new RecyclerviewAssertion(equalTo(1)));
    }

    @Test
    public void testFullGalleryMultipleImages() {
        TestUser.set("user2");
        launchActivity();
        onView(allOf(withId(R.id.gallery_rv),isDescendantOfA(allOf(withId(R.id.images_fragment), isDescendantOfA(withId(R.id.layout_gallery)))))).check(matches(isDisplayed()));

        onView(allOf(withId(R.id.gallery_rv),isDescendantOfA(allOf(withId(R.id.images_fragment), isDescendantOfA(withId(R.id.layout_gallery)))))).check(new RecyclerviewAssertion(equalTo(2)));
    }



    private void launchActivity() {
        mActivityRule.launchActivity(null);
        mIdlingResource = mActivityRule.getActivity().getIdlingResource();
        IdlingRegistry.getInstance().register(mIdlingResource);
    }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(mIdlingResource);
        }
    }
    public static BoundedMatcher<View, ImageView> hasDrawable() {
        return new BoundedMatcher<View, ImageView>(ImageView.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("has drawable");
            }

            @Override
            public boolean matchesSafely(ImageView imageView) {
                return imageView.getDrawable() != null;
            }
        };
    }
}

