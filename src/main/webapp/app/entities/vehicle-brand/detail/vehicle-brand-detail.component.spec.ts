import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { VehicleBrandDetailComponent } from './vehicle-brand-detail.component';

describe('VehicleBrand Management Detail Component', () => {
  let comp: VehicleBrandDetailComponent;
  let fixture: ComponentFixture<VehicleBrandDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [VehicleBrandDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./vehicle-brand-detail.component').then(m => m.VehicleBrandDetailComponent),
              resolve: { vehicleBrand: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(VehicleBrandDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(VehicleBrandDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load vehicleBrand on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', VehicleBrandDetailComponent);

      // THEN
      expect(instance.vehicleBrand()).toEqual(expect.objectContaining({ id: 123 }));
    });
  });

  describe('PreviousState', () => {
    it('Should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
