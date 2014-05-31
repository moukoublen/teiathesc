package org.teiath.teiesc;

import org.teiath.teiesc.provider.dbtransaction.FullDbTranPack;

import android.content.ContentResolver;
import android.content.Context;

public final class DataToDataPack
{

    public static final void fillAll(DataPack itemPack, FullDbTranPack tr,
            Context c)
    {
        fillAll(itemPack, tr, c.getContentResolver());
    }

    public static final void fillAll(DataPack itemPack, FullDbTranPack tr,
            ContentResolver c)
    {
        tr.getLectures().getAll(c, itemPack.getLectures());
        tr.getLessons().getAll(c, itemPack.getLessons());
        tr.getRegistrations().getAll(c, itemPack.getRegistrations());
    }

}
