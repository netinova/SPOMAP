package Model;

import java.util.List;

public class UserLists {

    public static class UserAdminList {
        private List<AdminUser> users;

        public UserAdminList() {
        }

        public UserAdminList(List<AdminUser> users) {
            this.users = users;
        }

        public List<AdminUser> getUsers() {
            return users;
        }

        public void setUsers(List<AdminUser> users) {
            this.users = users;
        }
    }

    public static class UserNormalList {
        private List<NormalUser> users;

        public UserNormalList() {
        }

        public UserNormalList(List<NormalUser> users) {
            this.users = users;
        }

        public List<NormalUser> getUsers() {
            return users;
        }

        public void setUsers(List<NormalUser> users) {
            this.users = users;
        }
    }

    public static class UserPrimeList {
        private List<PrimeUser> users;

        public UserPrimeList() {
        }

        public UserPrimeList(List<PrimeUser> users) {
            this.users = users;
        }

        public List<PrimeUser> getUsers() {
            return users;
        }

        public void setUsers(List<PrimeUser> users) {
            this.users = users;
        }
    }

}
