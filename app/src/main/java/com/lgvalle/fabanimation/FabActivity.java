package com.lgvalle.fabanimation;

import android.animation.Animator;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBarActivity;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;

public class FabActivity extends ActionBarActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fab);

        getFragmentManager()
                .beginTransaction()
                .add(R.id.frag_content, TitleFragment.newInstance())
                .commit();
    }


    public static class TitleFragment extends Fragment {
        public static TitleFragment newInstance() {
            return new TitleFragment();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            final View view = inflater.inflate(R.layout.fragment_fab_title, container, false);
            final View fabbutton = view.findViewById(R.id.fab);
            fabbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final ControlsFragment controlsFragment = ControlsFragment.newInstance();

                    setupSharedElementTransition(controlsFragment);

                    getFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frag_content, controlsFragment)
                            .addSharedElement(fabbutton, "pause_button")
                            .commit();
                }
            });
            return view;
        }

        private void setupSharedElementTransition(final ControlsFragment controlsFragment) {
            Transition sharedTransition = TransitionInflater.from(getActivity()).inflateTransition(R.transition.shared_enter_transition);
            controlsFragment.setSharedElementEnterTransition(sharedTransition);
            controlsFragment.setSharedElementReturnTransition(sharedTransition);
            sharedTransition.addListener(new Transition.TransitionListener() {
                @Override
                public void onTransitionEnd(Transition transition) {
                    controlsFragment.revealContent();
                }

                @Override
                public void onTransitionStart(Transition transition) {
                }

                @Override
                public void onTransitionCancel(Transition transition) {
                }

                @Override
                public void onTransitionPause(Transition transition) {
                }

                @Override
                public void onTransitionResume(Transition transition) {
                }
            });

        }


    }

    public static class ControlsFragment extends Fragment {

        public static ControlsFragment newInstance() {
            return new ControlsFragment();
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_fab_controls, container, false);
        }


        public void revealContent() {
            View layout = getView().findViewById(R.id.controls_layout);
            animateRevealColor(layout);
        }

        private void animateRevealColor(View targetView) {
            int cx = (targetView.getLeft() + targetView.getRight()) / 2;
            int cy = (targetView.getTop() + targetView.getBottom()) / 2;

            cx += targetView.getTranslationX();
            cy += targetView.getTranslationY();
            int finalRadius = Math.max(targetView.getWidth(), targetView.getHeight());

            Animator anim = ViewAnimationUtils.createCircularReveal(targetView, cx, cy, 0, finalRadius);
            targetView.setBackgroundColor(getResources().getColor(R.color.accent_material_light));
            anim.setDuration(getResources().getInteger(R.integer.default_anim_duration));
            anim.setInterpolator(new AccelerateInterpolator());
            anim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationEnd(Animator animator) {
                    animateScaleButton(getView().findViewById(R.id.ff_button));
                    animateScaleButton(getView().findViewById(R.id.rew_button));
                }

                @Override
                public void onAnimationStart(Animator animator) {
                }

                @Override
                public void onAnimationCancel(Animator animator) {
                }

                @Override
                public void onAnimationRepeat(Animator animator) {
                }
            });
            anim.start();
        }

        private void animateScaleButton(View view) {
            ViewCompat.animate(view)
                    .scaleX(1)
                    .scaleY(1)
                    .setDuration(250)
                    .start();
        }
    }

}
