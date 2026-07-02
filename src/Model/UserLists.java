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

        public void addUser(NormalUser user) {
            users.add(user);
        }

        public void removeUser(NormalUser user) {
            users.remove(user);
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

        public void addUser(PrimeUser user) {
            users.add(user);
        }

        public void removeUser(PrimeUser user) {
            users.remove(user);
        }
    }

}
