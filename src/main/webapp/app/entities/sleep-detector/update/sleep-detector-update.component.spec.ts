import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IVehicle } from 'app/entities/vehicle/vehicle.model';
import { VehicleService } from 'app/entities/vehicle/service/vehicle.service';
import { IDriver } from 'app/entities/driver/driver.model';
import { DriverService } from 'app/entities/driver/service/driver.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { ISleepDetector } from '../sleep-detector.model';
import { SleepDetectorService } from '../service/sleep-detector.service';
import { SleepDetectorFormService } from './sleep-detector-form.service';

import { SleepDetectorUpdateComponent } from './sleep-detector-update.component';

describe('SleepDetector Management Update Component', () => {
  let comp: SleepDetectorUpdateComponent;
  let fixture: ComponentFixture<SleepDetectorUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let sleepDetectorFormService: SleepDetectorFormService;
  let sleepDetectorService: SleepDetectorService;
  let vehicleService: VehicleService;
  let driverService: DriverService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [SleepDetectorUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(SleepDetectorUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SleepDetectorUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    sleepDetectorFormService = TestBed.inject(SleepDetectorFormService);
    sleepDetectorService = TestBed.inject(SleepDetectorService);
    vehicleService = TestBed.inject(VehicleService);
    driverService = TestBed.inject(DriverService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call vehicle query and add missing value', () => {
      const sleepDetector: ISleepDetector = { id: 456 };
      const vehicle: IVehicle = { id: 3187 };
      sleepDetector.vehicle = vehicle;

      const vehicleCollection: IVehicle[] = [{ id: 19072 }];
      jest.spyOn(vehicleService, 'query').mockReturnValue(of(new HttpResponse({ body: vehicleCollection })));
      const expectedCollection: IVehicle[] = [vehicle, ...vehicleCollection];
      jest.spyOn(vehicleService, 'addVehicleToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ sleepDetector });
      comp.ngOnInit();

      expect(vehicleService.query).toHaveBeenCalled();
      expect(vehicleService.addVehicleToCollectionIfMissing).toHaveBeenCalledWith(vehicleCollection, vehicle);
      expect(comp.vehiclesCollection).toEqual(expectedCollection);
    });

    it('Should call driver query and add missing value', () => {
      const sleepDetector: ISleepDetector = { id: 456 };
      const driver: IDriver = { id: 26668 };
      sleepDetector.driver = driver;

      const driverCollection: IDriver[] = [{ id: 19029 }];
      jest.spyOn(driverService, 'query').mockReturnValue(of(new HttpResponse({ body: driverCollection })));
      const expectedCollection: IDriver[] = [driver, ...driverCollection];
      jest.spyOn(driverService, 'addDriverToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ sleepDetector });
      comp.ngOnInit();

      expect(driverService.query).toHaveBeenCalled();
      expect(driverService.addDriverToCollectionIfMissing).toHaveBeenCalledWith(driverCollection, driver);
      expect(comp.driversCollection).toEqual(expectedCollection);
    });

    it('Should call User query and add missing value', () => {
      const sleepDetector: ISleepDetector = { id: 456 };
      const user: IUser = { id: 12851 };
      sleepDetector.user = user;

      const userCollection: IUser[] = [{ id: 22935 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ sleepDetector });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining),
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const sleepDetector: ISleepDetector = { id: 456 };
      const vehicle: IVehicle = { id: 28144 };
      sleepDetector.vehicle = vehicle;
      const driver: IDriver = { id: 10156 };
      sleepDetector.driver = driver;
      const user: IUser = { id: 5372 };
      sleepDetector.user = user;

      activatedRoute.data = of({ sleepDetector });
      comp.ngOnInit();

      expect(comp.vehiclesCollection).toContain(vehicle);
      expect(comp.driversCollection).toContain(driver);
      expect(comp.usersSharedCollection).toContain(user);
      expect(comp.sleepDetector).toEqual(sleepDetector);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISleepDetector>>();
      const sleepDetector = { id: 123 };
      jest.spyOn(sleepDetectorFormService, 'getSleepDetector').mockReturnValue(sleepDetector);
      jest.spyOn(sleepDetectorService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sleepDetector });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sleepDetector }));
      saveSubject.complete();

      // THEN
      expect(sleepDetectorFormService.getSleepDetector).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(sleepDetectorService.update).toHaveBeenCalledWith(expect.objectContaining(sleepDetector));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISleepDetector>>();
      const sleepDetector = { id: 123 };
      jest.spyOn(sleepDetectorFormService, 'getSleepDetector').mockReturnValue({ id: null });
      jest.spyOn(sleepDetectorService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sleepDetector: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sleepDetector }));
      saveSubject.complete();

      // THEN
      expect(sleepDetectorFormService.getSleepDetector).toHaveBeenCalled();
      expect(sleepDetectorService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISleepDetector>>();
      const sleepDetector = { id: 123 };
      jest.spyOn(sleepDetectorService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sleepDetector });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(sleepDetectorService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareVehicle', () => {
      it('Should forward to vehicleService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(vehicleService, 'compareVehicle');
        comp.compareVehicle(entity, entity2);
        expect(vehicleService.compareVehicle).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareDriver', () => {
      it('Should forward to driverService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(driverService, 'compareDriver');
        comp.compareDriver(entity, entity2);
        expect(driverService.compareDriver).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareUser', () => {
      it('Should forward to userService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(userService, 'compareUser');
        comp.compareUser(entity, entity2);
        expect(userService.compareUser).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
