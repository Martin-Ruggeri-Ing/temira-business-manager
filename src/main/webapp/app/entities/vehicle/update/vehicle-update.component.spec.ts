import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { IVehicleType } from 'app/entities/vehicle-type/vehicle-type.model';
import { VehicleTypeService } from 'app/entities/vehicle-type/service/vehicle-type.service';
import { IVehicleBrand } from 'app/entities/vehicle-brand/vehicle-brand.model';
import { VehicleBrandService } from 'app/entities/vehicle-brand/service/vehicle-brand.service';
import { IVehicle } from '../vehicle.model';
import { VehicleService } from '../service/vehicle.service';
import { VehicleFormService } from './vehicle-form.service';

import { VehicleUpdateComponent } from './vehicle-update.component';

describe('Vehicle Management Update Component', () => {
  let comp: VehicleUpdateComponent;
  let fixture: ComponentFixture<VehicleUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let vehicleFormService: VehicleFormService;
  let vehicleService: VehicleService;
  let userService: UserService;
  let vehicleTypeService: VehicleTypeService;
  let vehicleBrandService: VehicleBrandService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [VehicleUpdateComponent],
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
      .overrideTemplate(VehicleUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(VehicleUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    vehicleFormService = TestBed.inject(VehicleFormService);
    vehicleService = TestBed.inject(VehicleService);
    userService = TestBed.inject(UserService);
    vehicleTypeService = TestBed.inject(VehicleTypeService);
    vehicleBrandService = TestBed.inject(VehicleBrandService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const vehicle: IVehicle = { id: 456 };
      const user: IUser = { id: 6967 };
      vehicle.user = user;

      const userCollection: IUser[] = [{ id: 20532 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ vehicle });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining),
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call VehicleType query and add missing value', () => {
      const vehicle: IVehicle = { id: 456 };
      const type: IVehicleType = { id: 11674 };
      vehicle.type = type;

      const vehicleTypeCollection: IVehicleType[] = [{ id: 18608 }];
      jest.spyOn(vehicleTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: vehicleTypeCollection })));
      const additionalVehicleTypes = [type];
      const expectedCollection: IVehicleType[] = [...additionalVehicleTypes, ...vehicleTypeCollection];
      jest.spyOn(vehicleTypeService, 'addVehicleTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ vehicle });
      comp.ngOnInit();

      expect(vehicleTypeService.query).toHaveBeenCalled();
      expect(vehicleTypeService.addVehicleTypeToCollectionIfMissing).toHaveBeenCalledWith(
        vehicleTypeCollection,
        ...additionalVehicleTypes.map(expect.objectContaining),
      );
      expect(comp.vehicleTypesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call VehicleBrand query and add missing value', () => {
      const vehicle: IVehicle = { id: 456 };
      const brand: IVehicleBrand = { id: 23134 };
      vehicle.brand = brand;

      const vehicleBrandCollection: IVehicleBrand[] = [{ id: 31644 }];
      jest.spyOn(vehicleBrandService, 'query').mockReturnValue(of(new HttpResponse({ body: vehicleBrandCollection })));
      const additionalVehicleBrands = [brand];
      const expectedCollection: IVehicleBrand[] = [...additionalVehicleBrands, ...vehicleBrandCollection];
      jest.spyOn(vehicleBrandService, 'addVehicleBrandToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ vehicle });
      comp.ngOnInit();

      expect(vehicleBrandService.query).toHaveBeenCalled();
      expect(vehicleBrandService.addVehicleBrandToCollectionIfMissing).toHaveBeenCalledWith(
        vehicleBrandCollection,
        ...additionalVehicleBrands.map(expect.objectContaining),
      );
      expect(comp.vehicleBrandsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const vehicle: IVehicle = { id: 456 };
      const user: IUser = { id: 4394 };
      vehicle.user = user;
      const type: IVehicleType = { id: 11304 };
      vehicle.type = type;
      const brand: IVehicleBrand = { id: 21177 };
      vehicle.brand = brand;

      activatedRoute.data = of({ vehicle });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContain(user);
      expect(comp.vehicleTypesSharedCollection).toContain(type);
      expect(comp.vehicleBrandsSharedCollection).toContain(brand);
      expect(comp.vehicle).toEqual(vehicle);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVehicle>>();
      const vehicle = { id: 123 };
      jest.spyOn(vehicleFormService, 'getVehicle').mockReturnValue(vehicle);
      jest.spyOn(vehicleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vehicle });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: vehicle }));
      saveSubject.complete();

      // THEN
      expect(vehicleFormService.getVehicle).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(vehicleService.update).toHaveBeenCalledWith(expect.objectContaining(vehicle));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVehicle>>();
      const vehicle = { id: 123 };
      jest.spyOn(vehicleFormService, 'getVehicle').mockReturnValue({ id: null });
      jest.spyOn(vehicleService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vehicle: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: vehicle }));
      saveSubject.complete();

      // THEN
      expect(vehicleFormService.getVehicle).toHaveBeenCalled();
      expect(vehicleService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVehicle>>();
      const vehicle = { id: 123 };
      jest.spyOn(vehicleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vehicle });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(vehicleService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareUser', () => {
      it('Should forward to userService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(userService, 'compareUser');
        comp.compareUser(entity, entity2);
        expect(userService.compareUser).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareVehicleType', () => {
      it('Should forward to vehicleTypeService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(vehicleTypeService, 'compareVehicleType');
        comp.compareVehicleType(entity, entity2);
        expect(vehicleTypeService.compareVehicleType).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareVehicleBrand', () => {
      it('Should forward to vehicleBrandService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(vehicleBrandService, 'compareVehicleBrand');
        comp.compareVehicleBrand(entity, entity2);
        expect(vehicleBrandService.compareVehicleBrand).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
