package org.teiath.teiesc.provider.dbmetadata;

import org.teiath.teiesc.dataitems.IItemRefDictionary;
import org.teiath.teiesc.dataitems.DataItemReference;
import org.teiath.teiesc.utils.Utils;

public final class FullTablesPack implements IItemRefDictionary<ADbTableMetaData>
{
    public final TableLectures      tLectures;
    public final TableLessons       tLessons;
    public final TableRegistrations tRegistrations;
    public final TableRooms         tRooms;
    public final TableUpdates       tUpdates;
    
    public FullTablesPack()
    {
        this(new TableLectures(), new TableLessons(), new TableRegistrations(),
                new TableRooms(), new TableUpdates());
    }

    public FullTablesPack(TableLectures tableLectures,
            TableLessons tableLessons, TableRegistrations tableRegistrations,
            TableRooms tableRooms, TableUpdates tableUpdates)
    {
        this.tLectures = tableLectures;
        this.tLessons = tableLessons;
        this.tRegistrations = tableRegistrations;
        this.tRooms = tableRooms;
        this.tUpdates = tableUpdates;
    }
    
    public final String getTableName(DataItemReference tb)
    {
        ADbTableMetaData tbl = get(tb);
        return tbl == null ? "" : tbl.getTableName();
    }
    
    @Override
    public ADbTableMetaData get(DataItemReference tb)
    {
        switch (tb)
        {
        case LEC:
            return tLectures;
        case LES:
            return tLessons;
        case REG:
            return tRegistrations;
        case ROO:
            return tRooms;
        case UPD:
            return tUpdates;
        default:
            return null;
        }
    }
    
    public final ADbTableMetaData[] getAll()
    {
        return Utils.asArray(tLectures, tLessons, tRegistrations, tRooms, tUpdates);
    }
}
