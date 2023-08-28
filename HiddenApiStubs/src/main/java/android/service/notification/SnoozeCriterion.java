package android.service.notification;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author: Line'R
 * E-mail: serinity320@mail.com
 * Github: https://github.com/LinerSRT
 * Date: 28.08.2023, 10:33
 */
public final class SnoozeCriterion implements Parcelable {
    private final String mId;
    private final CharSequence mExplanation;
    private final CharSequence mConfirmation;

    public SnoozeCriterion(String id, CharSequence explanation, CharSequence confirmation) {
        mId = id;
        mExplanation = explanation;
        mConfirmation = confirmation;
    }

    protected SnoozeCriterion(Parcel in) {
        if (in.readByte() != 0) {
            mId = in.readString();
        } else {
            mId = null;
        }
        if (in.readByte() != 0) {
            mExplanation = in.readString();
        } else {
            mExplanation = null;
        }
        if (in.readByte() != 0) {
            mConfirmation = in.readString();
        } else {
            mConfirmation = null;
        }
    }


    public String getId() {
        return mId;
    }


    public CharSequence getExplanation() {
        return mExplanation;
    }


    public CharSequence getConfirmation() {
        return mConfirmation;
    }

    public static final Creator<SnoozeCriterion> CREATOR = new Creator<SnoozeCriterion>() {
        @Override
        public SnoozeCriterion createFromParcel(Parcel in) {
            return new SnoozeCriterion(in);
        }

        @Override
        public SnoozeCriterion[] newArray(int size) {
            return new SnoozeCriterion[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    @Override
    public boolean equals(Object o) {
        return false;
    }

    @Override
    public int hashCode() {
        return 0;
    }
}