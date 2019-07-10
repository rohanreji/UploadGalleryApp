package com.themaskedbit.uploadgalleryapp.gallery.view;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.themaskedbit.uploadgalleryapp.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class FabButtonTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(
                    MainActivity.class);

    @Test
    public void fabInitVisible() {
        onView(withId(R.id.fab)).check(matches(isDisplayed()));
    }

    @Test
    public void testFabClick() {
        //pressing fab button
        onView(withId(R.id.fab)).perform(click());
        //checking that dialog box appears
        onView(withText(R.string.dialog_upload_title)).check(matches(isDisplayed()));
        onView(withText(R.string.dialog_upload_message)).check(matches(isDisplayed()));
        onView(withText(R.string.dialog_camera)).check(matches(isDisplayed()));
        onView(withText(R.string.dialog_gallery)).check(matches(isDisplayed()));
    }
}
