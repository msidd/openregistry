package org.openregistry.security;

import javax.persistence.*;

/**
 * @version $Revision$ $Date$
 * @since 1.0.0
 */
@Entity(name="permission")
@Table(name="or_permissions")
public final class JpaPermissionImpl implements Permission {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "or_permissions_seq")
    @SequenceGenerator(name="or_permissions_seq",sequenceName="or_permissions_seq",initialValue=1,allocationSize=50)
    private long id;

    @Column(name="permission_type")
    private PermissionType permissionType;

    @Column(name="value")
    private String value;

    @ManyToOne(optional=false)
    @JoinColumn(name="sor_id")
    private JpaSystemOfRecordImpl systemOfRecord;

    @Column(name="permission")
    private String permission;

    @Column(name="description")
    private String description;

    @Column(name="p_create")
    private boolean create;

    @Column(name="p_read")
    private boolean read;

    @Column(name="p_update")
    private boolean update;

    @Column(name="p_delete")
    private boolean delete;

    public String getUser() {
        if (getPermissionType() == PermissionType.USER) {
            return this.value;
        }

        return null;
    }

    public PermissionType getPermissionType() {
        return this.permissionType;
    }

    public String getSystemOfRecord() {
        return this.systemOfRecord.getName();
    }

    public String getPermission() {
        return this.permission;
    }

    public String getDescription() {
        return this.description;
    }

    public String getExpression() {
        if (getPermissionType() == PermissionType.EXPRESSION) {
            return this.value;
        }
        return null;
    }

    public boolean isCreate() {
        return this.create;
    }

    public boolean isRead() {
        return this.read;
    }

    public boolean isUpdate() {
        return this.update;
    }

    public boolean isDelete() {
        return this.delete;
    }

    // TODO implement
    public int compareTo(final Permission permission) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
