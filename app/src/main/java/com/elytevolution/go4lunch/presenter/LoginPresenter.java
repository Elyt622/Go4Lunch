package com.elytevolution.go4lunch.presenter;

import android.content.Intent;

import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;

import java.util.Objects;

import androidx.constraintlayout.widget.ConstraintLayout;

import static android.app.Activity.RESULT_OK;

public class LoginPresenter {

    //FOR DATA
    // 1 - Identifier for Sign-In Activity
    private static final int RC_SIGN_IN = 123;
    private LoginPresenter.View view;
    public LoginPresenter(LoginPresenter.View view) {
        this.view = view;
    }

    // 3 - Method that handles response after SignIn Activity close
    public void handleResponseAfterSignIn(ConstraintLayout constraintLayout, int requestCode, int resultCode, Intent data){
        IdpResponse response = IdpResponse.fromResultIntent(data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) { // SUCCESS
                view.showSnackBar(constraintLayout, "Succès");
            } else { // ERRORS
                if (response == null) {
                    view.showSnackBar(constraintLayout, "Annulé");
                } else if (Objects.requireNonNull(response.getError()).getErrorCode() == ErrorCodes.NO_NETWORK) {
                    view.showSnackBar(constraintLayout, "Pas Internet");
                } else if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    view.showSnackBar(constraintLayout, "Erreur Inconnue");
                }
            }
        }
    }

    public interface View {
        public void showSnackBar(ConstraintLayout constraintLayout, String message);
    }
}
