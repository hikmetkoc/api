package tr.com.meteor.crm.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.meteor.crm.domain.User;
import tr.com.meteor.crm.repository.UserRepository;
import tr.com.meteor.crm.security.SecurityUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(rollbackFor = Exception.class)
public class BaseUserService {

    private final UserRepository userRepository;

    public BaseUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public Optional<User> getUserWithRoles(Long id) {
        return userRepository.findOneWithRolesById(id);
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public Optional<User> getUserWithRoles() {
        return SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneWithRolesByLogin);
    }

    public Optional<User> getUserFullFetched(Long id) {
        return userRepository.findAll().stream().filter(x -> x.getId() != null && x.getId().equals(id)).findFirst();
    }

    public List<User> getHierarchicalUsersOnlyDownwards(User user) {
        List<User> allUsers = userRepository.findAll();

        return getHierarchicalUsersOnlyDownwardsRecursive(user, allUsers, new ArrayList<>());
    }

    public List<User> getHierarchicalUsers(User user) {
        List<User> allUsers = userRepository.findAll();

        List<User> users = getHierarchicalUsersOnlyDownwardsRecursive(user, allUsers, new ArrayList<>());

        for (User u : getHierarchicalUsersOnlyUpwardsRecursive(user, allUsers, new ArrayList<>())) {
            if (users.stream().noneMatch(x -> x.getId().equals(u.getId()))) {
                users.add(u);
            }
        }

        return users;
    }

    private List<User> getHierarchicalUsersOnlyDownwardsRecursive(User user, List<User> allUsers, List<User> path) {
        if (path.stream().noneMatch(x -> x.getId().equals(user.getId()))) {
            Optional<User> currentUser = allUsers.stream().filter(x -> x.getId().equals(user.getId())).findFirst();

            if (currentUser.isPresent()) {
                path.add(currentUser.get());

                for (User child : currentUser.get().getMembers()) {
                    path = getHierarchicalUsersOnlyDownwardsRecursive(child, allUsers, path);
                }
            }
        }

        return path;
    }

    private List<User> getHierarchicalUsersOnlyUpwardsRecursive(User user, List<User> allUsers, List<User> path) {
        if (path.stream().noneMatch(x -> x.getId().equals(user.getId()))) {
            Optional<User> currentUser = allUsers.stream().filter(x -> x.getId().equals(user.getId())).findFirst();

            if (currentUser.isPresent()) {
                path.add(currentUser.get());

                for (User child : currentUser.get().getGroups()) {
                    path = getHierarchicalUsersOnlyUpwardsRecursive(child, allUsers, path);
                }
            }
        }

        return path;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
