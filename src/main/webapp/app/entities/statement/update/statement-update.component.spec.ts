import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ISleepDetector } from 'app/entities/sleep-detector/sleep-detector.model';
import { SleepDetectorService } from 'app/entities/sleep-detector/service/sleep-detector.service';
import { IVehicle } from 'app/entities/vehicle/vehicle.model';
import { VehicleService } from 'app/entities/vehicle/service/vehicle.service';
import { IDriver } from 'app/entities/driver/driver.model';
import { DriverService } from 'app/entities/driver/service/driver.service';
import { IStatement } from '../statement.model';
import { StatementService } from '../service/statement.service';
import { StatementFormService } from './statement-form.service';

import { StatementUpdateComponent } from './statement-update.component';

describe('Statement Management Update Component', () => {
  let comp: StatementUpdateComponent;
  let fixture: ComponentFixture<StatementUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let statementFormService: StatementFormService;
  let statementService: StatementService;
  let sleepDetectorService: SleepDetectorService;
  let vehicleService: VehicleService;
  let driverService: DriverService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [StatementUpdateComponent],
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
      .overrideTemplate(StatementUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(StatementUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    statementFormService = TestBed.inject(StatementFormService);
    statementService = TestBed.inject(StatementService);
    sleepDetectorService = TestBed.inject(SleepDetectorService);
    vehicleService = TestBed.inject(VehicleService);
    driverService = TestBed.inject(DriverService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call SleepDetector query and add missing value', () => {
      const statement: IStatement = { id: 456 };
      const sleepDetector: ISleepDetector = { id: 22878 };
      statement.sleepDetector = sleepDetector;

      const sleepDetectorCollection: ISleepDetector[] = [{ id: 24965 }];
      jest.spyOn(sleepDetectorService, 'query').mockReturnValue(of(new HttpResponse({ body: sleepDetectorCollection })));
      const additionalSleepDetectors = [sleepDetector];
      const expectedCollection: ISleepDetector[] = [...additionalSleepDetectors, ...sleepDetectorCollection];
      jest.spyOn(sleepDetectorService, 'addSleepDetectorToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ statement });
      comp.ngOnInit();

      expect(sleepDetectorService.query).toHaveBeenCalled();
      expect(sleepDetectorService.addSleepDetectorToCollectionIfMissing).toHaveBeenCalledWith(
        sleepDetectorCollection,
        ...additionalSleepDetectors.map(expect.objectContaining),
      );
      expect(comp.sleepDetectorsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Vehicle query and add missing value', () => {
      const statement: IStatement = { id: 456 };
      const vehicle: IVehicle = { id: 29605 };
      statement.vehicle = vehicle;

      const vehicleCollection: IVehicle[] = [{ id: 11848 }];
      jest.spyOn(vehicleService, 'query').mockReturnValue(of(new HttpResponse({ body: vehicleCollection })));
      const additionalVehicles = [vehicle];
      const expectedCollection: IVehicle[] = [...additionalVehicles, ...vehicleCollection];
      jest.spyOn(vehicleService, 'addVehicleToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ statement });
      comp.ngOnInit();

      expect(vehicleService.query).toHaveBeenCalled();
      expect(vehicleService.addVehicleToCollectionIfMissing).toHaveBeenCalledWith(
        vehicleCollection,
        ...additionalVehicles.map(expect.objectContaining),
      );
      expect(comp.vehiclesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Driver query and add missing value', () => {
      const statement: IStatement = { id: 456 };
      const driver: IDriver = { id: 11263 };
      statement.driver = driver;

      const driverCollection: IDriver[] = [{ id: 12201 }];
      jest.spyOn(driverService, 'query').mockReturnValue(of(new HttpResponse({ body: driverCollection })));
      const additionalDrivers = [driver];
      const expectedCollection: IDriver[] = [...additionalDrivers, ...driverCollection];
      jest.spyOn(driverService, 'addDriverToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ statement });
      comp.ngOnInit();

      expect(driverService.query).toHaveBeenCalled();
      expect(driverService.addDriverToCollectionIfMissing).toHaveBeenCalledWith(
        driverCollection,
        ...additionalDrivers.map(expect.objectContaining),
      );
      expect(comp.driversSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const statement: IStatement = { id: 456 };
      const sleepDetector: ISleepDetector = { id: 20378 };
      statement.sleepDetector = sleepDetector;
      const vehicle: IVehicle = { id: 3908 };
      statement.vehicle = vehicle;
      const driver: IDriver = { id: 22322 };
      statement.driver = driver;

      activatedRoute.data = of({ statement });
      comp.ngOnInit();

      expect(comp.sleepDetectorsSharedCollection).toContain(sleepDetector);
      expect(comp.vehiclesSharedCollection).toContain(vehicle);
      expect(comp.driversSharedCollection).toContain(driver);
      expect(comp.statement).toEqual(statement);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IStatement>>();
      const statement = { id: 123 };
      jest.spyOn(statementFormService, 'getStatement').mockReturnValue(statement);
      jest.spyOn(statementService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ statement });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: statement }));
      saveSubject.complete();

      // THEN
      expect(statementFormService.getStatement).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(statementService.update).toHaveBeenCalledWith(expect.objectContaining(statement));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IStatement>>();
      const statement = { id: 123 };
      jest.spyOn(statementFormService, 'getStatement').mockReturnValue({ id: null });
      jest.spyOn(statementService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ statement: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: statement }));
      saveSubject.complete();

      // THEN
      expect(statementFormService.getStatement).toHaveBeenCalled();
      expect(statementService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IStatement>>();
      const statement = { id: 123 };
      jest.spyOn(statementService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ statement });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(statementService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareSleepDetector', () => {
      it('Should forward to sleepDetectorService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(sleepDetectorService, 'compareSleepDetector');
        comp.compareSleepDetector(entity, entity2);
        expect(sleepDetectorService.compareSleepDetector).toHaveBeenCalledWith(entity, entity2);
      });
    });

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
  });
});
