package com.amb.kevyorganizer.presentation.camera

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.amb.camera.KevyCameraView
import com.amb.kevyorganizer.R
import com.amb.kevyorganizer.data.CameraFragmentViewModel
import kotlinx.android.synthetic.main.fragment_camera.*

class CameraFragment : Fragment() {

    private val captureButton by lazy { view?.findViewById(R.id.captureButton) as ImageView }
    private val kevyCameraView by lazy { view?.findViewById(R.id.kevyCameraView) as KevyCameraView }

    private var currentProductId: Long = 0L
    private lateinit var viewModel: CameraFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_camera, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(CameraFragmentViewModel::class.java)
        startCamera()
        setupCaptureButton()
        getProductFromArguments()
    }

    private fun getProductFromArguments() {
        arguments?.let {
            currentProductId = CameraFragmentArgs.fromBundle(it).currentProductId
        }
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
        viewModel.updateProductPhoto(currentProductId, path)
        popBackStack()
    }

    private fun popBackStack() {
        Navigation.findNavController(cameraRoot).popBackStack()
    }

    companion object {
        const val FOLDER_NAME = "KevyOrganizerFolder"
    }

}
