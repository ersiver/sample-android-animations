/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.samples.propertyanimation

import android.animation.*
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var mover: ObjectAnimator
    private lateinit var rotator: ObjectAnimator
    private lateinit var set: AnimatorSet

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        rotateButton.setOnClickListener {
            rotater()
        }

        translateButton.setOnClickListener {
            translater()
        }

        scaleButton.setOnClickListener {
            scaler()
        }

        fadeButton.setOnClickListener {
            fader()
        }

        colorizeButton.setOnClickListener {
            colorizer()
        }

        showerButton.setOnClickListener {
            shower()
        }
    }

    // extension function on ObjectAnimator. This makes the function more concise to call, since it
    // eliminates a parameter. It also makes the code a little more natural to read, by putting
    // the animator-related functionality directly onto ObjectAnimator.
    private fun ObjectAnimator.disableViewDuringAnimation(view: View) {
        addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?) {
                view.isEnabled = false
            }

            override fun onAnimationEnd(animation: Animator?) {
                view.isEnabled = true
            }
        })


    }

    private fun rotater() {
        val animator = ObjectAnimator.ofFloat(star, View.ROTATION, -360F, 0F)
        //-360 -> full circle clockwise
        animator.duration = 1000L //ratations takes 1 sekond

        //Whenever user click the button, the star reset to the 360 degr. which looks bad.
        //disable button to prevent from clicking while the animation is still running.
        //Or chack the current degr. and start rotation from the current value.

        //Disable/enable button using animator listener setup in the extension function
        animator.disableViewDuringAnimation(rotateButton)

        animator.start()
    }

    //Cause the start to move back and forth (200 pixels distance to the right)
    private fun translater() {

        val animator = ObjectAnimator.ofFloat(star, View.TRANSLATION_X, 200F)

        //Repetition is a way to telling animations to do the same task again and again.
        //We can set how many repetitions we want, and the specific behaviour e.g. restart, reverse etc
        animator.repeatCount = 1
        animator.repeatMode = ObjectAnimator.REVERSE

        //Prevent restarts while animation is running using animator listener setup in the extension function
        animator.disableViewDuringAnimation(translateButton)

        animator.start()
    }


    // when an object is scaled, it is usually scaled in x and y simultaneously, to avoid making it look "stretched"
    private fun scaler() {
        //In order to pass more than one behaviour to the AnimatorObject, use PropertyValuesHolder,
        //that will store those information. Here we pass information that the ster wi scale to
        //4 times of its size in X axis and Y axis,
        val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 4F)
        val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 4F)

        //Use multiple PropertyValuesHolders in a single animatior, to cause them animate in parallel.
        val animator = ObjectAnimator.ofPropertyValuesHolder(star, scaleX, scaleY)

        //Reset to the original size
        animator.repeatCount = 1
        animator.repeatMode = ObjectAnimator.REVERSE

        animator.disableViewDuringAnimation(scaleButton)

        animator.start()
    }


    //fade the star out to be completely transparent, and then back to fully opaque.
    private fun fader() {
        val animator = ObjectAnimator.ofFloat(star, View.ALPHA, 0F)

        animator.duration = 1000L

        //reset to full opaque
        animator.repeatCount = 1
        animator.repeatMode = ObjectAnimator.REVERSE

        animator.disableViewDuringAnimation(fadeButton)

        animator.start()

    }

    //Change color of the star background from black to red and reset,
    private fun colorizer() {
        // ObjectAnimator can animate literally anything that has setter/getter/
        // For animating color values use ofArgb()
        // pass the name of the property as string "backgroundColor"
        //that will be mapped to the appropriate setter: View.setBacgroundColor(int)
        val animator = ObjectAnimator.ofArgb(
            star.parent,
            "backgroundColor", Color.BLACK, Color.RED
        ).apply {
            duration = 500L
            repeatCount = 1
            repeatMode = ObjectAnimator.REVERSE
            disableViewDuringAnimation(colorizeButton)
        }.start()

    }

    // Animating multiple properties on multiple objects (randomed size stars).
    //new star and new animation is created everytime we click on the button
    private fun shower() {
        //1. Init a reference to the star field ViewGroup (which is just the parent of the current star view).
        val container = star.parent as ViewGroup
        star.visibility = View.GONE

        //2. Get dimensions of the container
        val containerW = container.width
        val containerH = container.height

        //3. Original size of the star icon, which will be scaled to get different sized stars
        var starW = star.width.toFloat()
        var starH = star.height.toFloat()

        //4. Create a ImageViews programmatically and add it to the parent view
        //That view will be created in the top left corner (0,0)
        val newStar1 = AppCompatImageView(this)
        newStar1.setImageResource(R.drawable.ic_star)
        newStar1.layoutParams =
            FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            )

        val newStar2 = AppCompatImageView(this)
        newStar2.setImageResource(R.drawable.ic_star)
        newStar2.layoutParams =
            FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            )

        val newStar3 = AppCompatImageView(this)
        newStar3.setImageResource(R.drawable.ic_star)
        newStar3.layoutParams =
            FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            )

        val newStar4 = AppCompatImageView(this)
        newStar4.setImageResource(R.drawable.ic_star)
        newStar4.layoutParams =
            FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            )

        val newStar5 = AppCompatImageView(this)
        newStar5.setImageResource(R.drawable.ic_star)
        newStar5.layoutParams =
            FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            )

        val stars = ArrayList<View>()
        stars.add(newStar1)
        stars.add(newStar2)
        stars.add(newStar3)
        stars.add(newStar4)
        stars.add(newStar5)

        for (star in stars) {

            container.addView(star)

            // Calculate random size of the icon
            star.scaleX = Math.random().toFloat() * 1.5F + 0.1F
            star.scaleY = star.scaleX

            starW *= star.scaleX
            starH *= star.scaleY

            //Icon view will be positioned in the top left corner (0,0) unless we set different.
            //Y is always 0 and x will be random:
            star.translationX = Math.random().toFloat() * (containerW - starW / 2)

            //Create animators for star falling down using interpolator for simulating gravity pulling
            //code animates the start from starting position -starH to ending container+startH which places the
            //icon off the container.
            mover =
                ObjectAnimator.ofFloat(star, View.TRANSLATION_Y, -starH, containerH + starH)
            mover.interpolator = AccelerateInterpolator(1F)

            //Create animator for star rotation with use of interpolator for smooth linear motion
            //The star will rotate a random amount between 0 and 1080 degrees (3 times around).
            rotator =
                ObjectAnimator.ofFloat(star, View.ROTATION, (Math.random() * 1080).toFloat())
            rotator.interpolator = LinearInterpolator()

            //Put two animators together with use of AnimatorSet
            set = AnimatorSet()
            set.playTogether(mover, rotator)
            set.duration = (Math.random() * 1500 + 500).toLong()

            //Once star reach has fallen outside the container it should be removed. Use listener to handle that
            set.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    container.removeView(star)
                }
            })

            set.start()
        }
    }

}
