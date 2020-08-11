package com.amb.kevyorganizer.presentation.camera

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.amb.camera.KevyCameraView
import com.amb.kevyorganizer.R
import kotlinx.android.synthetic.main.fragment_camera.*

class CameraFragment : Fragment() {

    private val captureButton by lazy { view?.findViewById(R.id.captureButton) as ImageView }
    private val kevyCameraView by lazy { view?.findViewById(R.id.kevyCameraView) as KevyCameraView }
    private lateinit var onCaptureSuccessListener: CameraAction

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_camera, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startCamera()
        setupCaptureButton()
    }

    override fun onDetach() {
        super.onDetach()
        kevyCameraView.stop()
    }

    private fun startCamera() {
        kevyCameraView.start()
    }

    private fun setupCaptureButton() {
        captureButton.setOnClickListener {
            kevyCameraView.capturePhoto(
                onCaptureSuccess = { path ->
                    handleOnCaptureSuccess(path)
                },
                folderName = FOLDER_NAME
            )
        }
    }

    private fun handleOnCaptureSuccess(path: String) {
        onCaptureSuccessListener.onCaptureSuccess(path)
    }

    private fun popBackStack() {
        Navigation.findNavController(cameraRoot).popBackStack()
    }

    fun setCaptureSuccessListener(listener: CameraAction) {
        onCaptureSuccessListener = listener
    }

    companion object {
        const val FOLDER_NAME = "KevyOrganizerFolder"
        private const val PERMISSION_REQUEST_CODE = 200
    }


    //todo
    //https://stackoverflow.com/questions/50754523/how-to-get-a-result-from-fragment-using-navigation-architecture-component
//    If navigating from Fragment A to Fragment B and A needs a result from B:
//
//    findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Type>("key")?.observe(viewLifecycleOwner) {result ->
//        // Do something with the result.
//    }
//    If on Fragment B and need to set the result:
//
//    findNavController().previousBackStackEntry?.savedStateHandle?.set("key", result)
//    I ended up creating two extensions for this:
//
//    fun Fragment.getNavigationResult(key: String = "result") =
//        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>(key)
//
//    fun Fragment.setNavigationResult(result: String, key: String = "result") {
//        findNavController().previousBackStackEntry?.savedStateHandle?.set(key, result)
//    }

}
