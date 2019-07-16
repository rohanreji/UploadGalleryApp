package com.themaskedbit.uploadgalleryapp.gallery.view;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.themaskedbit.uploadgalleryapp.R;
import com.themaskedbit.uploadgalleryapp.gallery.model.TestUser;

import org.hamcrest.Description;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.AllOf.allOf;

@RunWith(AndroidJUnit4.class)
public class GalleryUploadTest {
    private IdlingResource idlingResource;

    @Rule
    public IntentsTestRule<MainActivity> intentsRule = new IntentsTestRule<>(MainActivity.class);

    @Before
    public void stubIntents() {

        TestUser.set("user3");
        intending(not(isInternal())).respondWith(
                new Instrumentation.ActivityResult(Activity.RESULT_OK, null));
        Instrumentation.ActivityResult resultFromCamera = commonImageResult();
        intending(hasAction(MediaStore.ACTION_IMAGE_CAPTURE)).respondWith(resultFromCamera);

        //UI automation for granting permissions for SDK greater than M
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getInstrumentation().getUiAutomation().executeShellCommand(
                    "pm grant " + ApplicationProvider.getApplicationContext().getPackageName()
                            + " android.permission.READ_EXTERNAL_STORAGE");
            getInstrumentation().getUiAutomation().executeShellCommand(
                    "pm grant " + ApplicationProvider.getApplicationContext().getPackageName()
                            + " android.permission.WRITE_EXTERNAL_STORAGE");
            getInstrumentation().getUiAutomation().executeShellCommand(
                    "pm grant " + ApplicationProvider.getApplicationContext().getPackageName()
                            + " android.permission.CAMERA");
        }

        idlingResource = intentsRule.getActivity().getIdlingResource();
        IdlingRegistry.getInstance().register(idlingResource);
    }

    private Instrumentation.ActivityResult commonImageResult() {
        Bundle bundle = new Bundle();

        Resources resources = ApplicationProvider.getApplicationContext().getResources();
        Uri imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                resources.getResourcePackageName(R.mipmap.ic_launcher) + '/' +
                resources.getResourceTypeName(R.mipmap.ic_launcher) + '/' +
                resources.getResourceEntryName(R.mipmap.ic_launcher));
        bundle.putParcelable(MainActivity.IMAGE_EDITOR, imageUri);

        Intent resultData = new Intent();
        resultData.setData(imageUri);
        resultData.putExtras(bundle);

        return new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData);
    }

    @Test
    public void testGalleryImageSaveFailed() {

        onView(withId(R.id.fab)).perform(click());
        onView(withText(R.string.dialog_upload_message)).check(matches(isDisplayed()));
        onView(withText(R.string.dialog_camera)).perform(click());
        TestUser.set("error");
        onView(withId(R.id.editor_save)).check(matches(isDisplayed()));
        onView(withId(R.id.editor_save)).perform(click());
        onView(allOf(withId(R.id.gallery_rv), isDescendantOfA(allOf(withId(R.id.images_fragment), isDescendantOfA(withId(R.id.layout_gallery)))))).check(doesNotExist());
        onView(withText(R.string.dialog_connect_error)).check(matches(isDisplayed()));
        onView(withText(R.string.dialog_upload_error_message)).check(matches(isDisplayed()));
    }

    @Test
    public void testGalleryImageSaveSuccess() {

        onView(withId(R.id.fab)).perform(click());
        onView(withText(R.string.dialog_upload_message)).check(matches(isDisplayed()));
        onView(withText(R.string.dialog_camera)).perform(click());
        onView(withId(R.id.editor_save)).check(matches(isDisplayed()));
        onView(withId(R.id.editor_save)).perform(click());
        onView(withId(R.id.layout_gallery)).check(matches(isDisplayed()));
    }

    @After
    public void unregisterIdlingResource() {
        if (idlingResource != null) {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }

    //Helper to match whether a view has drawable.
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
