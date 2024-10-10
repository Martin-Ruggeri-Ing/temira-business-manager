import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { SleepDetectorDetailComponent } from './sleep-detector-detail.component';

describe('SleepDetector Management Detail Component', () => {
  let comp: SleepDetectorDetailComponent;
  let fixture: ComponentFixture<SleepDetectorDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SleepDetectorDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./sleep-detector-detail.component').then(m => m.SleepDetectorDetailComponent),
              resolve: { sleepDetector: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(SleepDetectorDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SleepDetectorDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load sleepDetector on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', SleepDetectorDetailComponent);

      // THEN
      expect(instance.sleepDetector()).toEqual(expect.objectContaining({ id: 123 }));
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
