/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.dakaik.rest.impl;

import gt.dakaik.common.Common;
import gt.dakaik.common.CommonEncripta;
import gt.dakaik.exceptions.EntidadNoEncontradaException;
import gt.dakaik.rest.interfaces.WSInit;
import gt.dakaik.rest.repository.AddressRepository;
import gt.dakaik.rest.repository.CityRepository;
import gt.dakaik.rest.repository.CountryRepository;
import gt.dakaik.rest.repository.LicenceRepository;
import gt.dakaik.rest.repository.MenuProfileRepository;
import gt.dakaik.rest.repository.MenuRepository;
import gt.dakaik.rest.repository.ProfileRepository;
import gt.dakaik.rest.repository.SchoolRepository;
import gt.dakaik.rest.repository.StateRepository;
import gt.dakaik.rest.repository.UserProfileRepository;
import gt.dakaik.rest.repository.UserRepository;
import gt.entities.Address;
import gt.entities.City;
import gt.entities.Country;
import gt.entities.Licences;
import gt.entities.Menu;
import gt.entities.Person;
import gt.entities.Profile;
import gt.entities.ProfileMenu;
import gt.entities.School;
import gt.entities.State;
import gt.entities.Status;
import gt.entities.User;
import gt.entities.UserProfile;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 *
 * @author Dario Calderon
 */
@Component
public class InitImpl implements WSInit {

    Logger eLog = LoggerFactory.getLogger(this.getClass());
    @Autowired
    UserRepository repoU;
    @Autowired
    CountryRepository repoCountry;
    @Autowired
    StateRepository repoState;
    @Autowired
    CityRepository repoCity;
    @Autowired
    AddressRepository repoAddress;
    @Autowired
    SchoolRepository repoSchool;
    @Autowired
    ProfileRepository repoProfile;
    @Autowired
    LicenceRepository repoLicence;
    @Autowired
    UserProfileRepository repoUProfile;
    @Autowired
    MenuRepository repoMenu;
    @Autowired
    MenuProfileRepository repoPMenu;

    @Override
    public ResponseEntity<String> findById(int idUsuario, String token) throws EntidadNoEncontradaException {
        if (!token.equals("dac3rkrDarioC3TokenInitS20")) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        if (idUsuario != -1500) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        Country country = new Country();
        country.setTxtName("Rt");
        repoCountry.save(country);

        State state = new State();
        state.setTxtName("Rt");
        state.setCountry(country);
        repoState.save(state);

        City city = new City();
        city.setTxtName("Rt");
        city.setState(state);
        repoCity.save(city);

        Address address = new Address();
        address.setCity(city);
        address.setIntZone(0);
        address.setTxtColony("");
        address.setTxtIndications("");
        address.setTxtNumberHouse("");
        repoAddress.save(address);

        School school = new School();
        school.setAddress(address);
        school.setNit("");
        school.setSnActive(Boolean.TRUE);
        school.setTxtName("Root");
        repoSchool.save(school);

        Person person = new Person();
        person.setAddress(address);
        person.setSnActive(true);
        person.setTxtFirstNameF("Root");
        person.setTxtFirstNameS("");
        person.setTxtFirstNameT("");
        person.setTxtFullName("Root");
        person.setTxtLastNameF("");
        person.setTxtLastNameS("");
        person.setTxtLastNameT("");

        User user = new User();
        user.setIntDaysChangePwd(360);
        user.setLastDatePwd(new Date());
        user.setPerson(person);
        user.setSnActive(Boolean.TRUE);
        user.setSnChangePwd(Boolean.FALSE);
        user.setTxtImageURI("");
        user.setTxtUser("dc@e.com.gt");
        String pwd = CommonEncripta.get_md5("dac3rkrRoot");
        user.setTxtPwd(pwd);
        repoU.save(user);

        Profile profile = new Profile();
        profile.setTxtDescription("Root");
        profile.setSnActive(Boolean.TRUE);
        repoProfile.save(profile);

        Licences lic = new Licences();
        lic.setDates(new Date());
        lic.setProfile(profile);
        lic.setSchool(school);
        lic.setSnActive(true);
        lic.setStatus(Status.A);
        lic.setTxtLicence(Common.getToken());
        repoLicence.save(lic);

        UserProfile uprofile = new UserProfile();
        uprofile.setUser(user);
        uprofile.setProfile(profile);
        uprofile.setLicence(lic);
        uprofile.setSnActive(true);
        repoUProfile.save(uprofile);

        Menu menuRoot = new Menu("menu.root", "", "", "", "", "", "", new Long(0), Boolean.TRUE);
        repoMenu.save(menuRoot);
        Menu menuMenu = new Menu("menu.menu", "app.menu", "mdi-action-view-module", "", "", "ctrlMenus", "pvpages/admin/menu/menu.html", menuRoot.getIdMenu(), Boolean.TRUE);
        repoMenu.save(menuMenu);
        Menu menuProfile = new Menu("menu.profile", "app.profile", "mdi-social-people", "", "", "ctrlProfile", "pvpages/admin/profile/profile.html", menuRoot.getIdMenu(), Boolean.TRUE);
        repoMenu.save(menuProfile);
        ProfileMenu pmenuR = new ProfileMenu();
        pmenuR.setMenu(menuRoot);
        pmenuR.setProfile(profile);
        pmenuR.setSnActive(true);
        repoPMenu.save(pmenuR);
        ProfileMenu pmenuM = new ProfileMenu();
        pmenuM.setMenu(menuMenu);
        pmenuM.setProfile(profile);
        pmenuM.setSnActive(true);
        repoPMenu.save(pmenuM);
        ProfileMenu pmenuP = new ProfileMenu();
        pmenuP.setMenu(menuProfile);
        pmenuP.setProfile(profile);
        pmenuP.setSnActive(true);
        repoPMenu.save(pmenuP);
        
        return new ResponseEntity("Success", HttpStatus.OK);

    }

}
