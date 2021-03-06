import {Injectable} from '@angular/core';
import {ScreenComponent} from '../visualization/screen/screen.component';
import {ScreenType} from '../enum/ScreenType';
import {Object3D, Vector2, Vector3} from 'three';
import {Subject} from 'rxjs';

@Injectable()
export class ScreenInteractionService {



  constructor() {

  }

  private static readonly NOTHING = '';
  rightScreen: ScreenComponent;
  leftScreen: ScreenComponent;

  private tooltipPositionSubject = new Subject <Vector2>();
  public tooltipPosition$ = this.tooltipPositionSubject.asObservable();

  private cursorStateSubject = new Subject<{position: Vector3, visible: boolean, scale: number}>();
  public cursorState$ = this.cursorStateSubject.asObservable();

  private hoverHighlight: string;
  private highlightedElements: string[] = [];

  private highlightedElementsSubject = new Subject<string[]>();
  public highlightedElements$ = this.highlightedElementsSubject.asObservable();

  public setCursorState(pPosition: Vector3= null, pVisible = true, pScale = 1) {
    this.cursorStateSubject.next({position: pPosition, visible: pVisible, scale: pScale});
  }

  public setMouseHighlight(elementName: string) {
    if (this.hoverHighlight === elementName) {return; }
    const isFile = elementName.includes('.');
    const exists = elementName !== ScreenInteractionService.NOTHING;
    if (exists) {
      if (isFile) {
        this.hoverHighlight = elementName;
      } else {
        this.hoverHighlight = ScreenInteractionService.NOTHING;
      }
    } else {
      this.hoverHighlight = ScreenInteractionService.NOTHING;
    }
    this.emitHighlights();
  }

  public setTooltipVisibility(visible: boolean) {

  }

  public select(elementName: string) {
    const index = this.highlightedElements.indexOf(elementName);
    if (index === -1) {
      this.highlightedElements = [elementName];
    } else {
      this.highlightedElements = [];
    }
    this.emitHighlights();
  }

  public toggleSelect(elementName: string) {
    const index = this.highlightedElements.indexOf(elementName);
    if (index === -1) {
      this.highlightedElements.push(elementName);
    } else {
      this.highlightedElements.splice(index, 1);
    }
    this.emitHighlights();
  }

  public unselect(elementName: string) {
    const index = this.highlightedElements.indexOf(elementName);
    this.highlightedElements.splice(index, 1);
    this.emitHighlights();
  }

  public resetSelection() {
    this.highlightedElements = [];
    this.emitHighlights();
  }

  public getSelectedElementNames(): string[] {
    return this.highlightedElements;
  }

  private emitHighlights() {
    let emittedHighlights: string[] = [];
    emittedHighlights = emittedHighlights.concat(this.highlightedElements);
    // Make sure the hover highlight isn't emitted twice
    if (this.hoverHighlight !== '' && emittedHighlights.indexOf(this.hoverHighlight) < 0) {emittedHighlights.push(this.hoverHighlight); }
    this.highlightedElementsSubject.next(emittedHighlights);
  }

  public otherScreen(screen: ScreenComponent): ScreenComponent {
    if (screen.screenType === ScreenType.LEFT) {
      return this.rightScreen;
    } else if (screen.screenType === ScreenType.RIGHT) {
      return this.leftScreen;
    } else {
      return null;
    }
  }

  public otherType(screenType: ScreenType): ScreenType {
    if (screenType === ScreenType.RIGHT) {return ScreenType.LEFT; } else { return ScreenType.RIGHT; }
  }

  public addScreen(screen: ScreenComponent) {
    if (screen.screenType === ScreenType.RIGHT) {
      this.rightScreen = screen;
    } else if (screen.screenType === ScreenType.LEFT) {
      this.leftScreen = screen;
    }
  }

  public getBlocksOfName(name: string): {rightBlock: Object3D, leftBlock: Object3D} {
    let rightBlock;
    let leftBlock;
    if (this.rightScreen) {rightBlock = this.rightScreen.scene.getObjectByName(name); }
    if (this.leftScreen) {leftBlock = this.leftScreen.scene.getObjectByName(name); }
    return {rightBlock, leftBlock};
  }

  getCounterpart(block: Object3D): Object3D {
    const pair = this.getBlocksOfName(block.name);
    if (pair.leftBlock === block) {
      return pair.rightBlock;
    } else if (pair.rightBlock === block) {
      return pair.leftBlock;
    } else {
      return null;
    }
  }
}
