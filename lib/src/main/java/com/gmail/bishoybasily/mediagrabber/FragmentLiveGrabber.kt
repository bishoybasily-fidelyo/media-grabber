package com.gmail.bishoybasily.mediagrabber

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import io.reactivex.Observable
import io.reactivex.ObservableEmitter

class FragmentLiveGrabber : Fragment() {

    private lateinit var emitter: ObservableEmitter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == MediaGrabber.CODE) {
                if (data != null) {
                    data.getStringExtra(MediaGrabber.EXTRA)?.let { emitter.onNext(it) }
                    emitter.onComplete()
                    return
                }
            }
        }
        emitter.onError(Throwable("User cancelled"))
    }

    fun grap(): Observable<String> {
        return Observable.create {
            emitter = it
            startActivityForResult(Intent(activity, ActivityImageGrabber::class.java), MediaGrabber.CODE)
        }
    }

}